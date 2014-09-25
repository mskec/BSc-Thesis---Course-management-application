import grails.converters.JSON
import hr.courseman.domain.Category
import hr.courseman.domain.CourseDefinition
import hr.courseman.domain.CourseInstance
import hr.courseman.domain.CourseInstance.Status
import hr.courseman.domain.CourseUser
import hr.courseman.domain.CourseUser.Type
import hr.courseman.domain.Notification
import hr.courseman.domain.Notification.Receiver
import hr.courseman.domain.user.Role
import hr.courseman.domain.user.User

class BootStrap {

    def lecturers = []

    def init = { servletContext ->
        customSetup()


//        environments {
//            development {
                createUsers()
                createCategories()
                createCourseDefinitions()
                createCourseInstances()
                createCourseUsersAndNotifications()
//            }
//        }
    }

    private void customSetup() {
        JSON.registerObjectMarshaller(Date) {
            return it?.format("yyyy-MM-dd'T'HH:mm:ss.sss'Z'")
        }
    }


    private void createUsers() {
        Role adminRole, lecturerRole, userRole
        Role.withTransaction {
            adminRole = Role.findByAuthority('ROLE_ADMIN')
            if (!adminRole) {
                adminRole = new Role(authority: 'ROLE_ADMIN')
            }

            lecturerRole = Role.findByAuthority('ROLE_LECTURER')
            if (!lecturerRole) {
                lecturerRole = new Role(authority: 'ROLE_LECTURER')
            }

            userRole = Role.findByAuthority('ROLE_USER')
            if (!userRole) {
                userRole = new Role(authority: 'ROLE_USER')
            }
        }

        User.withTransaction {
            User mskec = User.findByUsername('mskec')
            if (!mskec) {
                mskec = new User(username: 'mskec', password: 'mskec', email: 'mskec@courseman.com', firstName: 'Martin', lastName: 'Skec')
                mskec.addToAuthorities(adminRole)
                mskec.save(flush: true, failOnError: true)
            }


            def lecturerNames = ['Richard', 'John', 'Michael']
            for (int i = 0; i < 3; i++) {
                User lecturer = new User(username: 'lecturer' + (i+1), password: 'lecturer' + (i+1), email: 'lecturer' + (i+1) + '@courseman.com', firstName: lecturerNames[i], lastName: 'Lecturer' + (i+1))
                lecturer.addToAuthorities(lecturerRole)
                lecturer.save(flush: true, failOnError: true)
                lecturers.add(lecturer)
            }

            def userNames = ['William', 'Sarah', 'Margaret', 'Nancy', 'William', 'Ruth', 'John', 'Gordon', 'Doug', 'Robin']
            for (int i = 0; i < 10; i++) {
                User lecturer = new User(username: 'user' + (i+1), password: 'user' + (i+1), email: 'user' + (i+1) + '@courseman.com', firstName: userNames[i], lastName: 'User' + (i+1))
                lecturer.addToAuthorities(userRole)
                lecturer.save(flush: true, failOnError: true)
            }
        }

    }

    private void createCategories() {

        Category.withTransaction {
            Category category1 = new Category(name: 'Grails').save(flush: true, failOnError: true)
            Category c1SubCategory1_1 = new Category(name: 'Basic', parent: category1).save(flush: true, failOnError: true)

            Category category2 = new Category(name: 'Java').save(flush: true, failOnError: true)
            Category category3 = new Category(name: 'Linux').save(flush: true, failOnError: true)

            Category category4 = new Category(name: 'Javascript').save(flush: true, failOnError: true)
            Category c1SubCategory4_1 = new Category(name: 'AngularJS', parent: category4).save(flush: true, failOnError: true)
            Category c1SubCategory4_2 = new Category(name: 'Backbone', parent: category4).save(flush: true, failOnError: true)
        }

    }

    private void createCourseDefinitions() {

        String[] shortDescriptions = [
            'This course provides a comprehensive overview of using the Groovy language and the Grails framework to rapidly create real-world web applications. ',
            'This course is an introduction to software engineering, using the Java programming language. Participants will learn the fundamentals of Java. The focus is on developing high quality, working software that solves real problems.',
            'Develop a good working knowledge of Linux using both the graphical interface and command line, covering the major Linux distribution families.',
            'In this course you will learn basics of AngularJS. AngularJS is an open-source web application framework that assists with creating single-page applications, one-page web applications that only require HTML, CSS, and JavaScript on the client side.',
            '']
        String[] descriptions = [
            'Students learn front-end technologies (MVC, Ajax, REST), persistence with GORM using Hibernate and NoSQL datastores, convention-over-configuration, and application and plugin development best practices. Topics include artifacts, internationalization, building and deploying, testing, integration, security, and performance. Groovy topics including dynamic and static typing, closures, DSLs, and the meta-object protocol. Grails is an Open Source, full stack, web application framework for the JVM. It takes advantage of the Groovy programming language and convention over configuration to provide a productive and stream-lined development experience.',
            'Java as a programming language. Java as a processor independent platform. Classes and objects - inheritance, polymorphism, encapsulation, hiding. Java collection framework - sets, lists, trees, stacks, queues, maps. Java Generics. Multithreading and multithreaded applications. Synchronization problems and synchronization techniques (mutexes, semaphores, barriers). Design and development of applications with graphical user interface: AWT and Swing; usage of existing and development of custom components. MVC paradigm. Test Driven Development (TDD). Working with files and file systems. Data streams. Distributed applications (java.net packet). Usage of UDP and TCP protocol. HTTP protocol. Design of Web applications. Java Servlets and Java Server Pages (JSP). Web forms. Apache Tomcat. Security in Web applications. Advanced technologies: ANT, Hibernate, MySQL. Working with relational databases. Mapping and storage of objects into relational databases (O/R mapping).',
            'This skill is oriented towards students with no knowledge of Linux and it\'s command line. To accomplish this goal basic concepts will be introduced, as well as tools necessary for efficient use of command line. At the end, graphical user interface will be presented, as well as remote work using ssh shell.',
            'AngularJS is a structural framework for dynamic web apps. It lets you use HTML as your template language and lets you extend HTML\'s syntax to express your application\'s components clearly and succinctly. Angular\'s data binding and dependency injection eliminate much of the code you currently have to write. And it all happens within the browser, making it an ideal partner with any server technology. Angular is what HTML would have been had it been designed for applications. HTML is a great declarative language for static documents. It does not contain much in the way of creating applications, and as a result building web applications is an exercise in what do I have to do to trick the browser into doing what I want?',
            '']

        CourseDefinition.withTransaction {
            CourseDefinition course1 = new CourseDefinition(
                name: 'Grails Framework basics', shortDescription: shortDescriptions[0],
                description: descriptions[0], category: Category.findByParentAndName(Category.findByParentAndName(null, 'Grails'), 'Basic'),
                keywords: ['Grails', 'Framework', 'Groovy']
            ).save(flush: true, failOnError: true)

            CourseDefinition course2 = new CourseDefinition(
                name: 'Introduction to Java Programming Language', shortDescription: shortDescriptions[1],
                description: descriptions[1], category: Category.findByParentAndName(null, 'Java'),
                keywords: ['Java', 'Language']
            ).save(flush: true, failOnError: true)

            CourseDefinition course3 = new CourseDefinition(
                name: 'Basic use of Linux operating system', shortDescription: shortDescriptions[2],
                description: descriptions[2], category: Category.findByParentAndName(null, 'Linux'),
                keywords: ['Linux', 'System', 'Operating', 'OS']
            ).save(flush: true, failOnError: true)

            CourseDefinition course4 = new CourseDefinition(
                name: 'Introduction to AngularJS', shortDescription: shortDescriptions[3],
                description: descriptions[3], category: Category.findByParentAndName(Category.findByParentAndName(null, 'Javascript'), 'AngularJS'),
                keywords: ['Javascript', 'AngularJS', 'framework', 'frontend', 'HTML', 'CSS']
            ).save(flush: true, failOnError: true)
        }

    }

    private void createCourseInstances() {

        CourseInstance.withTransaction {
            new CourseInstance(
                courseDefinition: CourseDefinition.findById(1),
                location: 'A101, FER',
                lecturer: lecturers[0],
                startTime: new Date().parse('dd.MM.yyyy HH:mm', '12.06.2014 10:00'),
                endTime: new Date().parse('dd.MM.yyyy HH:mm', '12.06.2014 11:00'),
                maxNumberOfParticipants: 20
            ).save(flush: true, failOnError: true)

            new CourseInstance(
                courseDefinition: CourseDefinition.findById(2),
                location: 'D270, FER',
                lecturer: lecturers[0],
                startTime: new Date().parse('dd.MM.yyyy HH:mm', '10.06.2014 10:00'),
                endTime: new Date().parse('dd.MM.yyyy HH:mm', '10.06.2014 11:00'),
                maxNumberOfParticipants: 30
            ).save(flush: true, failOnError: true)

            new CourseInstance(
                courseDefinition: CourseDefinition.findById(2),
                location: 'D270, FER',
                lecturer: lecturers[0],
                startTime: new Date().parse('dd.MM.yyyy HH:mm', '13.06.2014 14:00'),
                endTime: new Date().parse('dd.MM.yyyy HH:mm', '13.06.2014 18:00'),
                maxNumberOfParticipants: 30
            ).save(flush: true, failOnError: true)

            new CourseInstance(
                courseDefinition: CourseDefinition.findById(2),
                status: Status.CANCELLED,
                location: 'D259, FER',
                lecturer: lecturers[0],
                startTime: new Date().parse('dd.MM.yyyy HH:mm', '08.05.2014 10:00'),
                endTime: new Date().parse('dd.MM.yyyy HH:mm', '08.05.2014 11:00'),
                maxNumberOfParticipants: 40
            ).save(flush: true, failOnError: true)

            new CourseInstance(
                courseDefinition: CourseDefinition.findById(3),
                location: 'B4, FER',
                lecturer: lecturers[1],
                startTime: new Date().parse('dd.MM.yyyy HH:mm', '11.06.2014 10:00'),
                endTime: new Date().parse('dd.MM.yyyy HH:mm', '11.06.2014 12:00'),
                maxNumberOfParticipants: 25
            ).save(flush: true, failOnError: true)

            new CourseInstance(
                courseDefinition: CourseDefinition.findById(4),
                location: 'Lastovska 23, CROZ, 5. kat Parentium',
                lecturer: lecturers[2],
                startTime: new Date().parse('dd.MM.yyyy HH:mm', '09.06.2014 10:00'),
                endTime: new Date().parse('dd.MM.yyyy HH:mm', '09.06.2014 18:00'),
                maxNumberOfParticipants: 25
            ).save(flush: true, failOnError: true)
        }

    }

    private void createCourseUsersAndNotifications() {

        CourseUser.withTransaction {

            User user1 = User.findByLastName('User1')
            User user2 = User.findByLastName('User2')
            User user3 = User.findByLastName('User3')
            User user4 = User.findByLastName('User4')
            User user5 = User.findByLastName('User5')
            User user6 = User.findByLastName('User6')

            CourseInstance courseInstance1 = CourseInstance.findById(1)
            CourseInstance courseInstance2 = CourseInstance.findById(2)
            CourseInstance courseInstance6 = CourseInstance.findById(6)

            // CourseInstance #1
            new CourseUser(
                courseInstance: courseInstance1,
                user: user1,
                type: Type.REGISTRATION,
                active: true
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('User %s %s registered for %s course.', user1.firstName, user1.lastName, courseInstance1.courseDefinition.name),
                receiver: Receiver.ADMIN,
                user: user1,
                courseInstance: courseInstance1,
            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance1,
                user: user2,
                type: Type.REGISTRATION,
                active: true,
                comment: 'I am very excited about this course!'
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('User %s %s registered for %s course.', user2.firstName, user2.lastName, courseInstance1.courseDefinition.name),
                receiver: Receiver.ADMIN,
                user: user2,
                courseInstance: courseInstance1,
            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance1,
                user: user3,
                type: Type.REGISTRATION,
                active: true
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('User %s %s registered for %s course.', user3.firstName, user3.lastName, courseInstance1.courseDefinition.name),
                receiver: Receiver.ADMIN,
                user: user3,
                courseInstance: courseInstance1,
            ).save(flush: true, failOnError: true)


            // CourseInstance #2
            new CourseUser(
                courseInstance: courseInstance2,
                user: user4,
                type: Type.REGISTRATION,
                active: true,
                comment: 'Cool course!'
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('User %s %s registered for %s course.', user4.firstName, user4.lastName, courseInstance2.courseDefinition.name),
                receiver: Receiver.ADMIN,
                user: user4,
                courseInstance: courseInstance2,
            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance2,
                user: user5,
                type: Type.REGISTRATION,
                active: true,
                comment: 'This should be good.'
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('User %s %s registered for %s course.', user5.firstName, user5.lastName, courseInstance2.courseDefinition.name),
                receiver: Receiver.ADMIN,
                user: user5,
                courseInstance: courseInstance2,
            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance2,
                user: user6,
                type: Type.REGISTRATION,
                active: true
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('User %s %s registered for %s course.', user6.firstName, user6.lastName, courseInstance2.courseDefinition.name),
                receiver: Receiver.ADMIN,
                user: user6,
                courseInstance: courseInstance2,
            ).save(flush: true, failOnError: true)


            // CourseInstance #3
//            new CourseUser(
//                courseInstance: courseInstance6,
//                user: user1,
//                type: Type.PRE_REGISTRATION,
//                active: false
//            ).save(flush: true, failOnError: true)
//            new Notification(
//                text: String.format('User %s %s preregistered for %s course.', user1.firstName, user1.lastName, courseInstance6.courseDefinition.name),
//                receiver: Receiver.ADMIN,
//                user: user1,
//                courseInstance: courseInstance6,
//            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance6,
                user: user1,
                type: Type.REGISTRATION,
                active: false
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('User %s %s registered for %s course.', user1.firstName, user1.lastName, courseInstance6.courseDefinition.name),
                receiver: Receiver.ADMIN,
                user: user1,
                courseInstance: courseInstance6,
            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance6,
                user: user1,
                type: Type.ACCEPTED,
                active: true
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('You are accepted at %s course.', courseInstance6.courseDefinition.name),
                receiver: Receiver.USER,
                user: user1,
                courseInstance: courseInstance6,
            ).save(flush: true, failOnError: true)

//            new CourseUser(
//                courseInstance: courseInstance6,
//                user: user2,
//                type: Type.PRE_REGISTRATION,
//                active: false
//            ).save(flush: true, failOnError: true)
//            new Notification(
//                text: String.format('User %s %s preregistered for %s course.', user2.firstName, user2.lastName, courseInstance6.courseDefinition.name),
//                receiver: Receiver.ADMIN,
//                user: user2,
//                courseInstance: courseInstance6,
//            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance6,
                user: user2,
                type: Type.REGISTRATION,
                active: false
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('User %s %s registered for %s course.', user2.firstName, user2.lastName, courseInstance6.courseDefinition.name),
                receiver: Receiver.ADMIN,
                user: user2,
                courseInstance: courseInstance6,
            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance6,
                user: user2,
                type: Type.ACCEPTED,
                active: true
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('You are accepted at %s course.', courseInstance6.courseDefinition.name),
                receiver: Receiver.USER,
                user: user2,
                courseInstance: courseInstance6,
            ).save(flush: true, failOnError: true)

//            new CourseUser(
//                courseInstance: courseInstance6,
//                user: user3,
//                type: Type.PRE_REGISTRATION,
//                active: false
//            ).save(flush: true, failOnError: true)
//            new Notification(
//                text: String.format('User %s %s preregistered for %s course.', user3.firstName, user3.lastName, courseInstance6.courseDefinition.name),
//                receiver: Receiver.ADMIN,
//                user: user3,
//                courseInstance: courseInstance6,
//            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance6,
                user: user3,
                type: Type.REGISTRATION,
                active: false
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('User %s %s registered for %s course.', user3.firstName, user3.lastName, courseInstance6.courseDefinition.name),
                receiver: Receiver.ADMIN,
                user: user3,
                courseInstance: courseInstance6,
            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance6,
                user: user3,
                type: Type.ACCEPTED,
                active: true
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('You are accepted at %s course.', courseInstance6.courseDefinition.name),
                receiver: Receiver.USER,
                user: user3,
                courseInstance: courseInstance6,
            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance6,
                user: user4,
                type: Type.REGISTRATION,
                active: false,
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('User %s %s registered for %s course.', user4.firstName, user4.lastName, courseInstance6.courseDefinition.name),
                receiver: Receiver.ADMIN,
                user: user4,
                courseInstance: courseInstance6,
            ).save(flush: true, failOnError: true)

            new CourseUser(
                courseInstance: courseInstance6,
                user: user4,
                type: Type.DECLINED,
                active: true,
                comment: 'Sorry, no more available seats.'
            ).save(flush: true, failOnError: true)
            new Notification(
                text: String.format('Sorry, no more available seats at %s course.', courseInstance6.courseDefinition.name),
                receiver: Receiver.USER,
                user: user4,
                courseInstance: courseInstance6,
            ).save(flush: true, failOnError: true)
        }

    }


    def destroy = {
    }
}
