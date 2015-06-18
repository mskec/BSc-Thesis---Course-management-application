package hr.courseman.domain.user

import hr.courseman.domain.CourseGrade
import hr.courseman.domain.CourseInstance
import hr.courseman.domain.CourseUser
import hr.courseman.domain.Notification

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    String firstName
    String lastName
    String email
    String avatar
    String description

    static hasMany = [authorities: Role, courseInstances: CourseInstance,
        notifications: Notification, courseUsers: CourseUser, courseGrades: CourseGrade]

    static transients = ['springSecurityService']

	static constraints = {
        username(blank: false, unique: true)
        password(blank: false)
        email(nullable: false, blank: false, email: true, unique: true)
        firstName(nullable: true, blank: true)
        lastName(nullable: true, blank: true)
        avatar(nullable: true, blank: true)
        description(nullable: true, blank: true)
	}

	static mapping = {
		password column: '`password`'
        avatar(type: 'text')
        description(type: 'text')
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}

    static marshalling = {
        shouldOutputClass false
        ignore('password', 'enabled', 'accountExpired', 'accountLocked', 'passwordExpired', 'description',
            'avatar', 'courseInstances', 'authorities', 'notifications', 'id', 'courseGrades', 'courseUsers')
        virtual {
            displayName { user, json ->
                json.value(user.firstName + ' ' + user.lastName)
            }
        }

        userDisplay {
            ignore('password', 'enabled', 'accountExpired', 'accountLocked', 'passwordExpired',
                'courseInstances', 'notifications', 'courseGrades', 'courseUsers')
            serializer {
                authorities { user, json ->
                    def authoritiesList = []
                    user.authorities.each {
                        authoritiesList.add(it.authority)
                    }
                    json.value(authoritiesList)
                }
            }
        }

    }
}
