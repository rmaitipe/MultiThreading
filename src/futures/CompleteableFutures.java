package futures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CompleteableFutures {

	public static void main (String [] args) throws InterruptedException, ExecutionException {
		Future<String> future = calculateAsync();
		String result = future.get();
		System.out.println(result);
	}
	
	public static Future<String> calculateAsync() throws InterruptedException {

	    CompletableFuture<String> completableFuture = new CompletableFuture<>();
	    Executors.newCachedThreadPool().submit(() -> {
	        Thread.sleep(500);
	        completableFuture.complete("Hello");
	        return null;
	    });

	    return completableFuture;
	}
}
