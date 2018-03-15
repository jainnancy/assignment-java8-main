package edu.knoldus;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * Operation on twitter.
 */
public class Operations {
    /**
     * twitter class object.
     */
    private static Twitter twitter = TwitterFactory.getSingleton();

    /**
     * query class oject.
     */
    private Query query;
    /**
     *  Constant integer hundred.
     */
    private final Integer hundred = 100;

    static {
        InputStream input = null;
        try {
            input = new FileInputStream("/home/knoldus/IdeaProjects/"
                    + "assignment-java-main/src/"
                    + "main/resources/application.config");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties properties = new Properties();
        try {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        twitter.setOAuthConsumer(properties.getProperty("consumerKey"),
                properties.getProperty("consumerSecret"));
        twitter.setOAuthAccessToken(new AccessToken(properties
                .getProperty("accessToken"),
                properties.getProperty("accessTokenSecret")));

    }

    /**
     * 1. Latest Post (Newer to Older) with limit.
     *
     * @param postLimit Post limit
     * @return CompletableFuture<ListIterator>
     */

    public final CompletableFuture<ListIterator>
    getLatestTweets(final Integer postLimit) {

        return CompletableFuture.supplyAsync(() -> {
            Integer limit = postLimit;
            Paging paging = new Paging(1, limit);
            ListIterator iterator = null;
            try {
                List tweets = twitter.getHomeTimeline(paging);
                iterator = tweets.listIterator();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return iterator;
        });
    }

    /**
     * 2. Older to Newer with limit and offset values.
     *
     * @param postLimit Post limit.
     * @return CompletableFuture<ListIterator> status list iterator.
     */
    public final CompletableFuture<ListIterator>
    getTweetsOlderToNewer(final Integer postLimit) {
        return CompletableFuture.supplyAsync(() -> {
            Integer limit = postLimit;
            final Long offset = 346316391L;
            Paging paging = new Paging(1, limit, offset);
            ListIterator iterator = null;
            try {
                List<Status> tweets = twitter.getHomeTimeline(paging);
                tweets.sort(Comparator.comparing(Status::getCreatedAt));
                iterator = tweets.listIterator();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return iterator;
        });
    }

    /**
     * 3. Number of Retweets (Higher to Lower)
     *
     * @param postLimit Post limit
     * @return CompletableFuture<ListIterator> Status iterator.
     */
    public final CompletableFuture<ListIterator>
    getretweetCountHighToLow(final Integer postLimit) {
        return CompletableFuture.supplyAsync(() -> {
            Integer limit = postLimit;
            Paging paging = new Paging(1, limit);
            ListIterator iterator = null;
            try {
                List<Status> tweets = twitter.getHomeTimeline(paging);
                tweets.sort(new Comparator<Status>() {
                    @Override
                    public int compare(final Status status1,
                                       final Status status2) {
                        return status2.getRetweetCount()
                                - status1.getRetweetCount();
                    }
                });
                iterator = tweets.listIterator();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return iterator;
        });
    }

    /**
     * 4. Number of Likes (Higher to Lower).
     *
     * @param postLimit Post limit
     * @return CompletableFuture<ListIterator> status listiterator
     */
    public final CompletableFuture<ListIterator>
    getLikeCountHighToLow(final Integer postLimit) {
        return CompletableFuture.supplyAsync(() -> {
            Integer limit = postLimit;
            Paging paging = new Paging(1, limit);
            ListIterator iterator = null;
            try {
                List<Status> tweets = twitter.getHomeTimeline(paging);
                tweets.sort(new Comparator<Status>() {
                    @Override
                    public int compare(final Status status1,
                                       final Status status2) {
                        return status2.getFavoriteCount()
                                - status1.getFavoriteCount();
                    }
                });
                iterator = tweets.listIterator();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return iterator;
        });
    }

    /**
     * 5. Get the List and number of tweets for an entered date.
     *
     * @param hashtag   hashtag
     * @param localDate localdate
     * @return CompletableFuture<List<Status>> tweets
     * @throws Exception exception
     */

    public final CompletableFuture<List<Status>>
    getDatedTweets(final String hashtag, final LocalDate localDate)
            throws Exception {
        return CompletableFuture.supplyAsync(() ->
        {
            query = new Query(hashtag);
            query.setCount(hundred);
            query.setSince(java.time.LocalDate
                    .of(localDate.getYear(),
                            localDate.getMonth(),
                            localDate.getDayOfMonth()).toString());
            query.setUntil(java.time.LocalDate
                    .of(localDate.getYear(),
                            localDate.getMonth(),
                            localDate.getDayOfMonth())
                    .plusDays(1)
                    .toString());
            QueryResult result = null;
            try {
                result = twitter.search(query);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return result.getTweets();
        });
    }


    /**
     * 6. Get the number of likes.
     * on a particular keyword in a time interval of 15 mins.
     * @param keyword keyword
     * @return CompletableFuture<Integer> totalLikes
     */
    public final CompletableFuture<Integer>
    get15minsLikeCountOnKeyword(final String keyword) {

        return CompletableFuture.supplyAsync(() ->
        {
            final Integer fifteen = 15;
            Integer totalLikes = 0;
            query = new Query(keyword);
            query.setCount(hundred);
            query.setSince(java.time.LocalTime.now()
                    .minusMinutes(fifteen)
                    .toString());
            query.setUntil(java.time.LocalTime.now().toString());
            QueryResult result = null;
            List<Status> tweets = null;
            try {
                result = twitter.search(query);
                tweets = result.getTweets();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            for (Status tweet : tweets) {
                totalLikes += tweet.getFavoriteCount();
            }
            return totalLikes;
        });
    }

}
