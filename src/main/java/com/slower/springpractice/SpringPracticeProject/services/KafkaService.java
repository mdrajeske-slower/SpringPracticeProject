package com.slower.springpractice.SpringPracticeProject.services;

import com.slower.springpractice.SpringPracticeProject.domain.kafka.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KafkaService {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private AdminClient adminClient;

    private final Map<String, Consumer<String, String>> consumerMap = new ConcurrentHashMap<>();

    private final ExecutorService consumerThreads = Executors.newCachedThreadPool();

    @PostConstruct
    private void createAdminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        adminClient = AdminClient.create(props);
    }

    public void createTopic(TopicConfig topicConfig){
        try(AdminClient adminClient = AdminClient.create(
                Collections.singletonMap(
                        AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers))) {
            NewTopic newTopic = new NewTopic(
                    topicConfig.getTopicName(),
                    topicConfig.getPartitions(),
                    topicConfig.getReplicationFactor());
            CreateTopicsResult result = adminClient.createTopics(Collections.singletonList(newTopic));
            result.all().get();
            System.out.println("Created topic " + topicConfig.getTopicName());
        }
        catch(Exception e){
            System.out.println("Error creating topic " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteTopic(String topicName){
        try(AdminClient adminClient = AdminClient.create(
                Collections.singletonMap(
                        AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers))) {
            DeleteTopicsResult result = adminClient.deleteTopics(Collections.singleton(topicName));
            result.all().get();
            System.out.println("Deleted topic " + topicName);
        }
        catch(Exception e){
            System.out.println("Error deleting topic " + e.getMessage());
        }
    }

    public void produceMessage(String message, String topicName) {
        for(int i = 0; i < 10000; i++) {
            kafkaTemplate.send(topicName, message);
        }
    }

    public KafkaStatus getStatus(){
        try {
            return KafkaStatus.builder()
                    .availBrokerCount(this.getNumberOfBrokers())
                    .underReplicatedPartitions(this.getUnderReplicatedPartitions())
                    .topicList(new ArrayList<>(this.getAllTopicNames()))
                    .build();
        }
        catch(Exception e){
            System.out.println("Error getting status " + e.getMessage());
            return null; // TODO: throw custom exception
        }
    }

    public void createConsumer(String topic, String groupId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        try {
            Consumer<String, String> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList(topic));
            consumerMap.put(groupId, consumer);
            consumerThreads.submit(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                        for (ConsumerRecord<String, String> record : records) {
                            System.out.println("Received message: " + record.value());
                        }
                    }
                } catch (WakeupException e) {
                    // Expected exception when shutting down
                    System.out.println("Consumer for group " + groupId + " is shutting down...");
                } finally {
                    consumer.close();
                }
            });
        }
        catch(Exception e){
            System.out.println("Error creating consumer " + e.getMessage());
        }
    }

    public ConsumerGroupInfo getConsumerGroupInfo(String groupId){
        try {
            DescribeConsumerGroupsResult result = adminClient.describeConsumerGroups(
                    java.util.Collections.singletonList(groupId)
            );

            Map<String, ConsumerGroupDescription> descriptions = result.all().get();
            for (ConsumerGroupDescription desc : descriptions.values()) {

                List<SimpleMemberDescription> members = desc.members().stream()
                        .map(member -> // Collect unique topic names
                                new SimpleMemberDescription(
                                        String.join(", ", member.assignment().topicPartitions().stream()
                                                .map(TopicPartition::topic) // Extracting topic name from partition
                                                .collect(Collectors.toSet())), // Join unique topic names
                                member.consumerId())) // Extracting member name (consumer ID)
                        .collect(Collectors.toList());

                return ConsumerGroupInfo.builder()
                        .groupId(groupId)
                        .members(members)
                        .build();
            }
        }
        catch(Exception e){
            System.out.println("Error getting consumer group info " + e.getMessage());
        }
        return null;
    }

    public void deleteConsumer(String groupId) {

        stopConsumer(groupId);

        System.out.println("Stopped all consumers in group " + groupId);
    }

    public void updateTopicConfig(TopicConfig topicConfig) {
        // TODO
    }

    public List<DiskUsage> getDiskUsageByTopic() {
        try {
            // Get the list of topics
            List<String> topics = new ArrayList<>(adminClient.listTopics().names().get());

            // Get the disk usage for each topic
            List<DiskUsage> diskUsages = new ArrayList<>();
            for (String topic : topics) {
                long totalDiskUsage = calculateTopicDiskUsage(topic);
                diskUsages.add(new DiskUsage(topic, totalDiskUsage));
            }

            return diskUsages;
        } catch (Exception e) {
            System.out.println("Error getting disk usage " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private long calculateTopicDiskUsage(String topic) {
        long totalDiskUsage = 0;

        try {
            // Get partition info for the topic
            List<TopicPartitionInfo> partitionInfos = adminClient.describeTopics(Collections.singleton(topic)).all().get().get(topic).partitions();

            // Iterate over each partition of the topic
            for (TopicPartitionInfo partitionInfo : partitionInfos) {
                // Get the log directory path for the specific partition
                String partitionDirPath = getPartitionLogDirPath(topic, partitionInfo.partition());
                File partitionDir = new File(partitionDirPath);

                if (partitionDir.exists() && partitionDir.isDirectory()) {
                    // Only calculate the size of the .log files in the partition's directory
                    totalDiskUsage += calculateLogFilesSize(partitionDir);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting disk usage " + e.getMessage());
        }

        // Return the total disk usage in kilobytes
        return totalDiskUsage / 1024;
    }

    private long calculateLogFilesSize(File directory) {
        long size = 0;

        // List all files in the directory
        File[] files = directory.listFiles();

        if (files != null) {
            // Loop through the files and sum the sizes of only .log files
            for (File file : files) {
                if (file.isDirectory()) {
                    // If it's a directory, recursively calculate its size
                    size += calculateLogFilesSize(file);
                } else if (file.getName().endsWith(".log")) {
                    // Only include log files (.log) in the size calculation
                    size += file.length();
                }
            }
        }

        return size;
    }

    private String getPartitionLogDirPath(String topic, int partitionId) {
        // This should return the correct path to the partition's log directory based on Kafka's log directory structure
        String baseLogDir = "/opt/homebrew/var/lib/kafka-logs"; // Replace with your actual log directory path
        return baseLogDir + "/" + topic + "-" + partitionId;
    }

    /**
     * Get the number of brokers in the cluster
     */
    public int getNumberOfBrokers() throws ExecutionException, InterruptedException {
        DescribeClusterResult clusterResult = adminClient.describeCluster();
        Collection<Node> nodes = clusterResult.nodes().get(); // Fetch broker nodes
        return nodes.size(); // Return the number of brokers
    }

    /**
     * Get the number of under-replicated partitions in the cluster
     */
    public int getUnderReplicatedPartitions() throws ExecutionException, InterruptedException {
        Map<String, TopicDescription> topics = adminClient.describeTopics(getAllTopicNames()).all().get();

        int underReplicatedPartitions = 0;

        for (TopicDescription topicDescription : topics.values()) {
            underReplicatedPartitions += (int) topicDescription.partitions().stream()
                    .filter(partition -> partition.replicas().size() > partition.isr().size()) // Compare total replicas with in-sync replicas
                    .count();
        }

        return underReplicatedPartitions;
    }

    /**
     * Helper method to fetch all topic names in the cluster
     */
    private Set<String> getAllTopicNames() throws ExecutionException, InterruptedException {
        return adminClient.listTopics().names().get();
    }

    private void stopConsumer(String groupId) {

        Consumer<String, String> consumer = consumerMap.get(groupId);
        if (consumer != null) {
            try {
                consumer.wakeup();
            } catch (Exception e) {
                System.out.println("Error stopping consumer " + groupId + ": " + e.getMessage());
            }
        } else {
            System.out.println("Consumer with group ID " + groupId + " not found.");
        }

    }



}
