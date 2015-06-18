package hr.courseman.domain

class Document {

    byte[] data
    String fileName
    Date uploadDate = new Date()

    static belongsTo = [courseInstance: CourseInstance]

    static constraints = {
        data(nullable: false, maxSize: 1024 * 1024 * 10)
        fileName(nullable: false, blank: false)
        uploadDate(nullable: false)
    }

    static mapping = {
        sort('uploadDate')
    }

    static marshalling = {
        shouldOutputClass false
        ignore 'data', 'courseInstance'
    }
}
