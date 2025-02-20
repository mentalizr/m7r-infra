package org.mentalizr.scheduler.mailNotifier;

import org.mentalizer.mailer.notifier.MailNotifierCallback;
import org.mentalizer.mailer.notifier.MailNotifierResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
public class SchedulerMailNotifierCallback implements MailNotifierCallback {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerMailNotifierCallback.class);

    @Override
    public void onSuccess(MailNotifierResult result) {
        logger.debug("Sent email notification with subject " +
                "[" + result.getMailNotification().getSubject() + "].");
    }

    @Override
    public void onFailure(MailNotifierResult result) {
        logger.error("Failed to send email notification with subject " +
                "[" + result.getMailNotification().getSubject() + "]." +
                " The following exception occurred.", result.getThrowable());
    }

}
