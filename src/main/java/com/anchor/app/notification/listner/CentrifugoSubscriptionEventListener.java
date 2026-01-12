package com.anchor.app.notification.listner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.centrifugal.centrifuge.JoinEvent;
import io.github.centrifugal.centrifuge.LeaveEvent;
import io.github.centrifugal.centrifuge.PublicationEvent;
import io.github.centrifugal.centrifuge.SubscribedEvent;
import io.github.centrifugal.centrifuge.SubscribingEvent;
import io.github.centrifugal.centrifuge.Subscription;
import io.github.centrifugal.centrifuge.SubscriptionErrorEvent;
import io.github.centrifugal.centrifuge.SubscriptionEventListener;
import io.github.centrifugal.centrifuge.UnsubscribedEvent;

public class CentrifugoSubscriptionEventListener extends SubscriptionEventListener  
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onError(Subscription sub, SubscriptionErrorEvent event) {
         logger.info("subscription error " + sub.getChannel() + " " + event.getError().toString());
    }

    @Override
    public void onJoin(Subscription sub, JoinEvent event) {
        logger.info("subscription client " + event.getInfo().getClient() + " joined channel " + sub.getChannel());
    }

    @Override
    public void onLeave(Subscription sub, LeaveEvent event) {
       logger.info("subscription client " + event.getInfo().getClient() + " left channel " + sub.getChannel());
    }

    @Override
    public void onPublication(Subscription sub, PublicationEvent event) {
        logger.info("subscription client " + event.getInfo().getClient() + " left channel " + sub.getChannel());
    }

    @Override
    public void onSubscribed(Subscription sub, SubscribedEvent event) {
        logger.info("subscription subscribed to " + sub.getChannel() + ", recovered " + event.getRecovered());
                String data="{\"input\": \"I just subscribed to channel\"}";
                sub.publish(data.getBytes(), (err, res) -> {
                    if (err != null) {
                        logger.info("error publish: " + err.getMessage());
                        return;
                    }
                    logger.info("successfully published");
                });
    }

    @Override
    public void onSubscribing(Subscription sub, SubscribingEvent event) {
         logger.info("subscription subscribing:"+event.getReason());
    }

    @Override
    public void onUnsubscribed(Subscription sub, UnsubscribedEvent event) {
         logger.info("subscription unsubscribed " + sub.getChannel() + ", reason: " + event.getReason());
    }
    

}
