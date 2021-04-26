package executable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/*
 * Executors are capable of running asynchronous tasks and typically manage a pool of threads, so we don't have to create new threads manually
 * In general, the ExecutorService will not be automatically destroyed when there is no task to process. 
 * It will stay alive and wait for new work to do. To properly shut down an ExecutorService, we have the shutdown() and shutdownNow() APIs.
 */
public class Threadpooling {

	public static void main (String [] args) throws InterruptedException, ExecutionException, TimeoutException {
		threadExample();
		usingFutures();
		try{
			asyncExampleFuturesTimeout();
		}
		catch (TimeoutException e) {
			System.out.println("TimeoutException!");
		}
		Map<String, Integer> map = new HashMap<>();
	    List<Integer> sumList = parallelSum100(map, 5);
	    sumList.stream().forEach(a-> System.out.println(a));
		Map<String, Integer> map2 = new ConcurrentHashMap<>();
	    List<Integer> sumList2 = parallelSum100(map2, 5);
	    sumList2.stream().forEach(a-> System.out.println(a));
	}
	
	public static void usingFutures() throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(10);	
		Future<String> future = executorService.submit(() -> "Hello World");//lambda Runnable
		String result = future.get();
		System.out.println(result);
		executorService.shutdown();
	}
	
	public static void threadExample() {
		Runnable task = () -> {
		    String threadName = Thread.currentThread().getName();
		    System.out.println("Hello " + threadName);
		};
		task.run();
		Thread thread = new Thread(task);
		thread.start();
		System.out.println("Done!");
	}
	
	public static void asyncExampleFuturesTimeout() throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<Integer> future = executor.submit(() -> {
		    try {
		        TimeUnit.SECONDS.sleep(2);
		        return 123;
		    }
		    catch (InterruptedException e) {
		        throw new IllegalStateException("task interrupted", e);
		    }
		});
		future.get(1, TimeUnit.SECONDS);
	}
	
	private static List<Integer> parallelSum100(Map<String, Integer> map, int executionTimes) throws InterruptedException {
	    List<Integer> sumList = new ArrayList<>(1000);
	    for (int i = 0; i < executionTimes; i++) {
	        map.put("test", 0);
	        ExecutorService executorService = Executors.newFixedThreadPool(4);
	        for (int j = 0; j < 10; j++) {
	            executorService.execute(() -> {
	                for (int k = 0; k < 10; k++)
	                    map.computeIfPresent("test", (key, value) -> value + 1);
	            });
	        }
	        executorService.shutdown();
	        executorService.awaitTermination(5, TimeUnit.SECONDS);
	        sumList.add(map.get("test"));
	    }
	    return sumList;
	}
	
}
