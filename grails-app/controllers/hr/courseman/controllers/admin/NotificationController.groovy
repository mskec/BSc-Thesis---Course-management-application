package hr.courseman.controllers.admin

import grails.converters.JSON
import grails.transaction.Transactional
import hr.courseman.controllers.UtilController
import hr.courseman.domain.Notification

@Transactional(readOnly = true)
class NotificationController {

    static namespace = 'admin'
    static allowedMethods = [list: 'GET', seen: 'POST']

    UtilController utilCtrl = new UtilController()

    def list() {
        boolean seen = Boolean.valueOf(params.seen)
        def notifications = Notification.findAllBySeenAndReceiver(seen, Notification.Receiver.ADMIN)

        JSON.use('displayForAdmin') {
            utilCtrl.respondJson(notifications)
        }
    }

    @Transactional
    def seen(Notification notification) {
        if (!notification || !notification.validate()) {
            utilCtrl.respondError('Something went wrong with request.')
            return
        }

        notification.setSeen(true)
        notification.save()

        utilCtrl.respondMessage('Notification saved as seen.')
    }


}
