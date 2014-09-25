package hr.courseman.controllers.admin

import grails.converters.JSON
import grails.transaction.Transactional
import hr.courseman.domain.user.Role
import hr.courseman.domain.user.User

@Transactional(readOnly = true)
class UserController {

    static namespace = 'admin'
    static allowedMethods = [lecturers: 'GET']

    def lecturers() {
        def lecturerList = User.executeQuery(
            'select u from User u where :authority in elements(authorities)', [authority: Role.findByAuthority('ROLE_LECTURER')])

        render lecturerList as JSON
    }

}
