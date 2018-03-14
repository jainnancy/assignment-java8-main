package edu.knoldus;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
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

public class Operations {

    final Integer hundred = 100;
    /**
     * twitter class object.
     */
    private static Twitter twitter = TwitterFactory.getSingleton();
    /**
     * query class oject.
     */
    static Query query;

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
     * @return CompletableFuture<ListIterator>
     */

    public final CompletableFuture<ListIterator>
    getLatestTweets(Integer postLimit) {

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
     */
    public final CompletableFuture<ListIterator> getTweetsOlderToNewer(Integer postLimit) {
        return CompletableFuture.supplyAsync(() -> {
            Integer limit = postLimit;
            Long offset = 346316391L;
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
     */
    public CompletableFuture<ListIterator> getretweetCountHighToLow(Integer postLimit) {
        return CompletableFuture.supplyAsync(() -> {
            Integer limit = postLimit;
            Paging paging = new Paging(1, limit);
            ListIterator iterator = null;
            try {
                List<Status> tweets = twitter.getHomeTimeline(paging);
                tweets.sort(new Comparator<Status>() {
                    @Override
                    public int compare(Status status1, Status status2) {
                        return status2.getRetweetCount() - status1.getRetweetCount();
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
     * 4. Number of Likes (Higher to Lower)
     */
    public CompletableFuture<ListIterator> getLikeCountHighToLow(Integer postLimit) {
        return CompletableFuture.supplyAsync(() -> {
            Integer limit = postLimit;
            Paging paging = new Paging(1, limit);
            ListIterator iterator = null;
            try {
                List<Status> tweets = twitter.getHomeTimeline(paging);
                tweets.sort(new Comparator<Status>() {
                    @Override
                    public int compare(Status status1, Status status2) {
                        return status2.getFavoriteCount() - status1.getFavoriteCount();
                    }
                });
                iterator = tweets.listIterator();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return iterator;
        });
    }

}
