package edu.knoldus;

import twitter4j.Status;

import java.util.ListIterator;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class TwitterApp {

    public static void main(final String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Limit: ");
        Integer limit = scanner.nextInt();
        /**
         * 1. Latest Post (Newer to Older) with limit.
         */
        System.out.println("1. Latest Post (Newer to Older) with limit.");
        CompletableFuture<ListIterator> iterator = new Operations().getLatestTweets(limit);
        while (iterator.get().hasNext()) {
            System.out.println(iterator.get().next());
        }

        /**
         * 2. Older to Newer with limit and offset values
         */
        System.out.println("\n\n2. Older to Newer with limit and offset values");
        CompletableFuture<ListIterator> oldToNewIterator = new Operations().getTweetsOlderToNewer(limit);
        while (oldToNewIterator.get().hasNext()) {
            System.out.println(oldToNewIterator.get().next());
        }

        /**
         * 3. Number of Retweets (Higher to Lower).
         */
        System.out.println("\n\n3. Number of Retweets (Higher to Lower)");
        CompletableFuture<ListIterator> retweetsHightoLow = new Operations().getretweetCountHighToLow(limit);
        while (retweetsHightoLow.get().hasNext()) {
            Status status = (Status)retweetsHightoLow.get().next();
            System.out.print(status.getRetweetCount() + " ");
        }

        /**
         * 4. Number of Likes (Higher to Lower)
         */
        System.out.println("\n\n4. Number of Likes (Higher to Lower)");
        CompletableFuture<ListIterator> likesHighToLow = new Operations().getLikeCountHighToLow(limit);
        while (likesHighToLow.get().hasNext()) {
            Status status = (Status)likesHighToLow.get().next();
            System.out.print(status.getFavoriteCount() + " ");
        }

        Thread.sleep(2000);
    }

}
