package edu.knoldus;

import twitter4j.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * Main class of the application.
 */
public class TwitterApp {

    /**
     * TwiitterApp constructor.
     */
    protected TwitterApp() {
    }

    /**
     *
     * @param args default string cmd.
     * @throws Exception Exception.
     */
    public static void main(final String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Limit: ");
        Integer limit = scanner.nextInt();
        final Integer sleepTime = 3000;
        /**
         * 1. Latest Post (Newer to Older) with limit.
         */
        System.out.println("1. Latest Post (Newer to Older) with limit.");
        CompletableFuture<ListIterator> iterator = new Operations()
                .getLatestTweets(limit);
        while (iterator.get().hasNext()) {
            System.out.println(iterator.get().next());
        }

        /**
         * 2. Older to Newer with limit and offset values
         */
        System.out.println("\n\n2. Older to Newer "
                + "with limit and offset values");
        CompletableFuture<ListIterator> oldToNewIterator = new Operations()
                .getTweetsOlderToNewer(limit);
        while (oldToNewIterator.get().hasNext()) {
            System.out.println(oldToNewIterator.get().next());
        }

        /**
         * 3. Number of Retweets (Higher to Lower).
         */
        System.out.println("\n\n3. Number of Retweets (Higher to Lower)");
        CompletableFuture<ListIterator> retweetsHightoLow = new Operations()
                .getretweetCountHighToLow(limit);
        while (retweetsHightoLow.get().hasNext()) {
            Status status = (Status) retweetsHightoLow.get().next();
            System.out.print(status.getRetweetCount() + " ");
        }

        /**
         * 4. Number of Likes (Higher to Lower)
         */
        System.out.println("\n\n4. Number of Likes (Higher to Lower)");
        CompletableFuture<ListIterator> likesHighToLow = new Operations()
                .getLikeCountHighToLow(limit);
        while (likesHighToLow.get().hasNext()) {
            Status status = (Status) likesHighToLow.get().next();
            System.out.print(status.getFavoriteCount() + " ");
        }

        /**
         * 5. Get the List and number of tweets for an entered date.
         */
        System.out.println("\n\n5. The List and number of "
                + "tweets for an entered date.");
        System.out.println("Enter keyword: ");
        String keyword = scanner.next();
        System.out.println("Enter year: ");
        Integer year = scanner.nextInt();
        System.out.println("Enter month: ");
        Integer month = scanner.nextInt();
        System.out.println("Enter date: ");
        Integer date = scanner.nextInt();
        LocalDate localDate = LocalDate.of(year, month, date);
        CompletableFuture<List<Status>> datedTweets = new Operations()
                .getDatedTweets("hawkins", localDate);
        datedTweets
                .thenAccept(System.out::println);

        /**
         * 6. Get the number of likes.
         * on a particular keyword in a time interval of 15 mins.
         */
        System.out.println("\n\n6. The number of likes on a "
                + "particular keyword in a time interval of 15 mins.");

        CompletableFuture<Integer> likeCount = new Operations()
                .get15minsLikeCountOnKeyword(keyword);
        //System.out.println(likeCount);
        likeCount.thenApply(totallikeCount -> "Like count of "
                + "last 15 minutes = " + totallikeCount)
                .thenAccept(System.out::println);

        Thread.sleep(sleepTime);
    }

}
