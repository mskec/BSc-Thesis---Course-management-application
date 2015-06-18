package hr.courseman.controllers.publc

import grails.converters.JSON
import grails.transaction.Transactional
import hr.courseman.controllers.UtilController
import hr.courseman.domain.user.Role
import hr.courseman.domain.user.User

@Transactional(readOnly = true)
class UserController {

    static namespace = 'public'
    static allowedMethods = [show: 'GET', save: 'POST']

    UtilController utilCtrl = new UtilController()

    def show() {
        if (params.id == null) {
            utilCtrl.respondError('User id not specified.')
            return
        }

        def user = User.findById(params.id)
        if (user == null) {
            utilCtrl.respondError('User not found.')
            return
        }

        JSON.use('userDisplay') {
            render user as JSON
        }
    }

    @Transactional
    def save(User user) {
        if (!user) {
            utilCtrl.respondError('Invalid request')
            return
        }

        if (!user.validate()) {
            utilCtrl.respondError(user.errors, 'Validation error.')
            return
        }

        def role = Role.findByAuthority('ROLE_USER')
        user.addToAuthorities(role)
        user.save()

        utilCtrl.respondMessage('You are successfully registered.')
    }

}
