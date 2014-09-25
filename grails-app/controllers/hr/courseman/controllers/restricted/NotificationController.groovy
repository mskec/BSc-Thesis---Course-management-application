package hr.courseman.controllers.restricted

import grails.transaction.Transactional
import hr.courseman.controllers.UtilController
import hr.courseman.domain.Notification
import hr.courseman.domain.Notification.Receiver
import hr.courseman.domain.user.User

@Transactional(readOnly = true)
class NotificationController {

    static namespace = 'restricted'
    static allowedMethods = [list: 'GET', count: 'GET', seen: 'POST']

    def springSecurityService
    def UtilController utilCtrl = new UtilController()

    def list() {
        boolean seen = Boolean.valueOf(params.seen)
        User user = springSecurityService.getCurrentUser()
        def notifications = Notification.findAllBySeenAndUserAndReceiver(seen, user, Receiver.USER)

        utilCtrl.respondJson(notifications)
    }

    def count() {
        User user = springSecurityService.getCurrentUser()
        def newNotificationCount = Notification.countBySeenAndUserAndReceiver(false, user, Receiver.USER)

        utilCtrl.respondJson([count: newNotificationCount])
    }

    @Transactional
    def seen(Notification notification) {
        if (!notification || !notification.validate()) {
            utilCtrl.respondError('Something went wrong with request.')
            return
        }

        User user = springSecurityService.getCurrentUser()
        if (user.id != notification.user.id) {
            utilCtrl.respondError('You can only manage your notifications.')
            return
        }

        notification.setSeen(true)
        notification.save()

        utilCtrl.respondMessage('Notification saved as seen.')
    }

}
