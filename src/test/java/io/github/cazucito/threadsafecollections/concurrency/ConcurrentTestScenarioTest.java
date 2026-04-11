package io.github.cazucito.threadsafecollections.concurrency;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para ConcurrentTestScenario.
 */
@DisplayName("ConcurrentTestScenario Tests")
class ConcurrentTestScenarioTest {

    @Test
    @DisplayName("Scenario with size verification reports correct size")
    void scenarioWithSizeVerificationReportsCorrectSize() {
        DemoResult.Builder result = DemoResult.builder("test", "Test Demo");
        AtomicInteger counter = new AtomicInteger(0);
        
        ConcurrentTestScenario scenario = ConcurrentTestScenario.withSizeVerification(5, 2);
        
        scenario.execute(result, index -> {
            counter.incrementAndGet();
            return () -> {};
        });
        
        assertEquals(5, counter.get(), "Should execute 5 tasks");
        
        List<MessageType> types = result.build().messages().stream()
                .map(m -> m.type())
                .toList();
        assertTrue(types.contains(MessageType.SUCCESS), "Should have success message for correct size");
    }

    @Test
    @DisplayName("Scenario executes all tasks")
    void scenarioExecutesAllTasks() {
        DemoResult.Builder result = DemoResult.builder("test", "Test Demo");
        List<String> executed = Collections.synchronizedList(new ArrayList<>());
        
        ConcurrentTestScenario scenario = ConcurrentTestScenario.withoutVerification(3, 2);
        
        scenario.execute(result, index -> {
            executed.add("Task " + index);
            return () -> {};
        });
        
        assertEquals(3, executed.size(), "Should execute all 3 tasks");
    }

    @Test
    @DisplayName("Scenario with collection executes tasks correctly")
    void scenarioWithCollectionExecutesTasksCorrectly() {
        DemoResult.Builder result = DemoResult.builder("test", "Test Demo");
        List<String> collection = Collections.synchronizedList(new ArrayList<>());
        
        ConcurrentTestScenario scenario = ConcurrentTestScenario.withoutVerification(3, 2);
        
        scenario.executeWithCollection(result, collection, (coll, index) -> {
            coll.add("Element " + index);
            return () -> {};
        });
        
        assertEquals(3, collection.size(), "Should add 3 elements to collection");
    }

    @Test
    @DisplayName("Scenario with single task executes correctly")
    void scenarioWithSingleTaskExecutesCorrectly() {
        DemoResult.Builder result = DemoResult.builder("test", "Test Demo");
        AtomicInteger counter = new AtomicInteger(0);
        
        ConcurrentTestScenario scenario = ConcurrentTestScenario.withoutVerification(1, 2);
        
        scenario.executeSingleTask(result, () -> counter.incrementAndGet());
        
        assertEquals(1, counter.get(), "Should execute single task once");
    }

    @Test
    @DisplayName("Thread count is accessible")
    void threadCountIsAccessible() {
        ConcurrentTestScenario scenario = ConcurrentTestScenario.withSizeVerification(10, 5);
        assertEquals(10, scenario.threadCount());
    }

    @Test
    @DisplayName("Timeout is accessible")
    void timeoutIsAccessible() {
        ConcurrentTestScenario scenario = ConcurrentTestScenario.withSizeVerification(5, 30);
        assertEquals(30, scenario.timeoutSeconds());
    }

    @Test
    @DisplayName("appendSizeResult reports success for matching sizes")
    void appendSizeResultReportsSuccessForMatchingSizes() {
        DemoResult.Builder result = DemoResult.builder("test", "Test Demo");
        
        ConcurrentTestScenario.appendSizeResult(result, 5, 5);
        
        List<MessageType> types = result.build().messages().stream()
                .map(m -> m.type())
                .toList();
        assertTrue(types.contains(MessageType.SUCCESS), "Should report success for matching sizes");
    }

    @Test
    @DisplayName("appendSizeResult reports logic error for mismatched sizes")
    void appendSizeResultReportsLogicErrorForMismatchedSizes() {
        DemoResult.Builder result = DemoResult.builder("test", "Test Demo");
        
        ConcurrentTestScenario.appendSizeResult(result, 5, 3);
        
        List<MessageType> types = result.build().messages().stream()
                .map(m -> m.type())
                .toList();
        assertTrue(types.contains(MessageType.LOGIC_ERROR), "Should report logic error for mismatched sizes");
    }

    @Test
    @DisplayName("Scenario does not throw on timeout")
    void scenarioDoesNotThrowOnTimeout() {
        DemoResult.Builder result = DemoResult.builder("test", "Test Demo");
        
        // Use a very short timeout that will likely timeout
        ConcurrentTestScenario scenario = ConcurrentTestScenario.withoutVerification(100, 1);
        
        assertDoesNotThrow(() -> {
            scenario.execute(result, index -> () -> {
                try {
                    Thread.sleep(1000); // Sleep longer than timeout
                } catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }, "Scenario should handle timeout gracefully");
    }
}