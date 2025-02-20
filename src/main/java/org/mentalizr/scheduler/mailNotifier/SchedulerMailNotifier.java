package org.mentalizr.scheduler.mailNotifier;

import org.mentalizer.mailer.notifier.MailNotifier;

public class SchedulerMailNotifier {

    public static void send(String subject, String text) {
        SchedulerMailNotifierCallback callback = new SchedulerMailNotifierCallback();
        MailNotifier.sendNotification(subject, text, callback);
    }

}
