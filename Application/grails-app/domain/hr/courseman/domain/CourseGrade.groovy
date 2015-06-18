package hr.courseman.domain

import hr.courseman.domain.user.User

class CourseGrade {

    Double grade
    Date gradingDate = new Date()

    static belongsTo = [user: User, courseInstance: CourseInstance]

    static constraints = {
        grade(min: 0d, max: 5d, scale: 2)
        gradingDate(nullable: false)
        // TODO stavi unique constraint za user i courseInstance
    }

    static marshalling = {
        shouldOutputClass false
        ignore 'gradingDate', 'user', 'courseInstance'
    }

}
