package hr.courseman.domain

import grails.converters.JSON

class Category {

    String name

    static belongsTo = [parent: Category]
    static hasMany = [subCategories: Category, courses: CourseDefinition]


    static constraints = {
        name(nullable: false, blank: false, maxSize: 255, unique: 'parent')
        parent(nullable: true)
    }

    static mapping = {
    }

    static marshalling = {
        // Default
        shouldOutputClass false
        attribute 'name'
        ignore 'parent', 'subCategories', 'courses'
        virtual {
            displayName { Category category, JSON json ->
                json.value(category.getDisplayName())
            }
        }

        adminCategoryShow {
            shouldOutputClass false
            attribute 'name'
            ignore 'parent', 'courses'
            deep 'subCategories'
            virtual {
                displayName { Category category, JSON json ->
                    json.value(category.getDisplayName())
                }
            }
        }

    }


    public String getDisplayName() {
        return parent == null ? name : parent.getDisplayName() + "/" + name
    }


}
