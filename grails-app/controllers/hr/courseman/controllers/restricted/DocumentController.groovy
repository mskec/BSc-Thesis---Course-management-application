package hr.courseman.controllers.restricted

import grails.converters.JSON
import grails.transaction.Transactional
import hr.courseman.controllers.UtilController
import hr.courseman.domain.CourseInstance
import hr.courseman.domain.Document

@Transactional(readOnly = true)
class DocumentController {

    static namespace = 'restricted'
    static allowedMethods = [download: 'GET', list: 'GET']

    UtilController utilCtrl = new UtilController()

    def download(Document document) {
        if (!document) {
            utilCtrl.respondError('Document not found')
            return
        }

        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "attachment; filename=${document.getFileName()}")
        response.setHeader("Content-Length", "${document.data.size()}")
        response.outputStream << document.data
    }

    def list(CourseInstance courseInstance) {
        if (!courseInstance || courseInstance.id == null) {
            utilCtrl.respondError('No documents found for given course instance.')
            return
        }

        def documentList = Document.findAllByCourseInstance(courseInstance);

        render documentList as JSON
    }
}
