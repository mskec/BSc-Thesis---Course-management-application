package hr.courseman.domain

import hr.courseman.domain.user.User
import hr.courseman.domain.CourseUser.Type

class CourseInstance {

    Status status = Status.UPCOMING
    Date startTime
    Date endTime
    String location
    int maxNumberOfParticipants
    Date lastModified = new Date()

    static hasOne = [lecturer: User]
    static belongsTo = [courseDefinition: CourseDefinition]
    static hasMany = [courseUsers: CourseUser, notifications: Notification, documents: Document, courseGrades: CourseGrade]

    static constraints = {
        status(nullable: false)
        startTime(nullable: false)
        endTime(nullable: true)
        location(nullable: false, blank: false, maxSize: 255)
        lastModified(nullable: false)
        maxNumberOfParticipants(nullable: false, min: 0)
        courseDefinition(nullable: false)
        lecturer(nullable: true)
    }

    static mapping = {
        sort('startTime')
    }

    static marshalling = {
        // Default
        shouldOutputClass false
        ignore 'courseDefinition', 'lastModified', 'courseUsers', 'notifications', 'documents', 'courseGrades'
        deep 'lecturer'
        serializer {
            status { courseInstance, json ->
                json.value(courseInstance.status.toString())
            }
        }

        adminDisplayCourseInstance {
            ignore 'courseUsers', 'notifications', 'documents', 'courseGrades'
            deep 'courseDefinition', 'lecturer'
            serializer {
                status { courseInstance, json ->
                    json.value(courseInstance.status.toString())
                }
            }
            virtual {
                preRegisteredUsers { courseInstance, json ->
                    json.value(courseInstance.courseUsers.findAll{ it.type == Type.PRE_REGISTRATION })
                }
                registeredUsers { courseInstance, json ->
                    json.value(courseInstance.courseUsers.findAll{ it.type == Type.REGISTRATION })
                }
                acceptedUsers { courseInstance, json ->
                    json.value(courseInstance.courseUsers.findAll{ it.type == Type.ACCEPTED })
                }
                declinedUsers { courseInstance, json ->
                    json.value(courseInstance.courseUsers.findAll{ it.type == Type.DECLINED })
                }
            }
        }

        // Public api serializers
        publicDisplay {
            ignore 'lastModified', 'courseUsers', 'courseDefinition', 'notifications', 'documents', 'courseGrades'
            deep 'lecturer'
            serializer {
                status { courseInstance, json ->
                    json.value(courseInstance.status.toString())
                }
            }
            virtual {
                course { courseInstance, json ->
                    json.value([id: courseInstance.courseDefinition.id, name: courseInstance.courseDefinition.name])
                }
                averageGrade { courseInstance, json ->
                    json.value(courseInstance.getAverageGrade())
                }
            }
        }

        publicListDisplay {
            ignore 'maxNumberOfParticipants', 'lastModified', 'courseUsers', 'notifications', 'documents', 'courseGrades'
            deep 'courseDefinition', 'lecturer'
            serializer {
                status { courseInstance, json ->
                    json.value(courseInstance.status.toString())
                }
            }
        }

    }

    def beforeUpdate() {
        lastModified = new Date()
    }

    public String getAverageGrade() {
        double gradeSum = 0
        courseGrades.each { courseGrade ->
            gradeSum += courseGrade.grade
        }
        return String.format("%.2f", (courseGrades.size() != 0 ? gradeSum / courseGrades.size() : 0.0))
    }

    enum Status {
        UPCOMING,
        HELD,
        CANCELLED
    }
}
