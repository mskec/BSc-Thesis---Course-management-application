package hr.courseman.domain


class CourseDefinition {

    String name
    String shortDescription
    String description

    static hasOne = [category: Category]
    static hasMany = [keywords: String, courseInstances: CourseInstance]

    static constraints = {
        name(nullable: false, blank: false, maxSize: 255, unique: 'category')
        shortDescription(nullable: false, blank: false, maxSize: 1024)
        description(nullable: true, blank: true)
        category(nullable: false)
    }

    static mapping = {
        description(type: "text")
    }

    static marshalling = {
        // Default
        shouldOutputClass false
        ignore 'shortDescription', 'description', 'keywords', 'category', 'courseInstances'

        adminDisplayCourseDefinition {
            deep 'category', 'courseInstances'
            ignore ''
        }

        adminListCourseDefinition {
            deep 'category'
            ignore 'courseInstances'
        }

        // Public api serializers
        publicDisplay {
            deep 'category'
            ignore 'courseInstances'
            virtual {
                upcoming { courseDefinition, json ->
                    json.value(courseDefinition.courseInstances.findAll{ it.status == CourseInstance.Status.UPCOMING })
                }
                averageGrade { courseDefinition, json ->
                    json.value(courseDefinition.getAverageGrade())
                }
            }
        }

        publicListDisplay {
            deep 'category'
            ignore 'courseInstances', 'description', 'keywords'
        }
    }

    public String getAverageGrade() {
        def heldInstances = courseInstances.findAll { courseInstance ->
            courseInstance.status == CourseInstance.Status.HELD
        }

        double gradeSum = 0
        heldInstances.each { courseInstance ->
            gradeSum += Double.valueOf(courseInstance.getAverageGrade())
        }

        return String.format("%.2f", (heldInstances.size() != 0 ? gradeSum / heldInstances.size() : 0.0))
    }
}
