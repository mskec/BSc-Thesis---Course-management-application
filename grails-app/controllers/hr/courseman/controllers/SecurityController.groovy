package hr.courseman.controllers

import grails.converters.JSON

import javax.servlet.http.Cookie

class SecurityController {

    static allowedMethods = [logout: 'POST']

    def springSecurityService
    UtilController utilCtrl = new UtilController()

    def ajaxSuccess() {
        def user = springSecurityService.getCurrentUser()
        response.setCookie('CourseMan', String.valueOf(user.id), -1)
//        Cookie cookie = new Cookie('CourseMan', String.valueOf(user.id))
//        cookie.setMaxAge(-1)
//        cookie.setSecure(true)
//        cookie.setPath('/CourseMan/')
//        cookie.setDomain('localhost')
//        response.addCookie(cookie)

        JSON.use('userDisplay') {
            utilCtrl.respondJson([successful: true, user: user])
        }
    }

    def logout() {
        response.deleteCookie('CourseMan')
        redirect(uri: '/j_security_logout')
    }

}
