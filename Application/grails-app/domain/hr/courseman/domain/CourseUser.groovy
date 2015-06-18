package hr.courseman.domain

import hr.courseman.domain.user.User

class CourseUser {

    Type type
    String comment
    boolean active = false
    Date creationDate = new Date()

    static belongsTo = [user: User, courseInstance: CourseInstance]

    static constraints = {
        type(nullable: false)
        comment(nullable: true, blank: true, maxSize: 255)
        active(nullable: false)
        creationDate(nullable: false)
        user(nullable: false, unique: ['courseInstance', 'type'])
    }

    static mapping = {
        sort('creationDate')
    }

    static marshalling = {
        shouldOutputClass false
        ignore 'courseInstance'
        deep 'user'
        serializer {
            type { courseUser, json ->
                json.value(courseUser.type.toString())
            }
        }
    }

    static enum Type {
        PRE_REGISTRATION,
        REGISTRATION,
        ACCEPTED,
        DECLINED
    }
}
