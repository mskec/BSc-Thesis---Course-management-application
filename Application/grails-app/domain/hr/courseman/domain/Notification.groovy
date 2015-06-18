package hr.courseman.domain

import hr.courseman.domain.user.User

class Notification {

    String text
    String comment
    Date creationDate = new Date()
    boolean seen = false
    boolean hide = false
    Receiver receiver
    static belongsTo = [user: User, courseInstance: CourseInstance]

    static constraints = {
        text(nullable: false, blank: false, maxSize: 255)
        comment(nullable: true, blank: true, maxSize: 255)
        creationDate(nullable: false)
        seen(nullable: false)
        hide(nullable: false)
        receiver(nullable: false)
        courseInstance(nullable: false)
        user(nullable: false)
    }

    static mapping = {
        sort(creationDate: 'desc')
    }

    static marshalling = {
        shouldOutputClass false
        ignore 'seen', 'hide', 'receiver', 'user', 'courseInstance'

        displayForAdmin {
            virtual {
                courseId { Notification notification, json ->
                    json.value(notification.courseInstance.courseDefinition.id)
                }
                courseInstanceId {Notification notification, json ->
                    json.value(notification.courseInstance.id)
                }
            }
        }
    }

    enum Receiver {
        ADMIN,
        USER
    }
}
