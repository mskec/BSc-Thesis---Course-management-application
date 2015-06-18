class UrlMappings {

	static mappings = {

        // Admin api
        // Restful mapping
        "/api/$namespace/$controller" {
            action = [GET: 'show', PUT: 'update', DELETE: 'delete', POST: 'save']
        }
        // Query mapping
        "/api/$namespace/$controller/$action" {
        }
        // END Admin api

        // Security mapping
        "/api/$controller/$action?" {
            constraints {
                controller(matches:/(security)/)
            }
        }

        // Login i logout spring security
        "/$controller/$action?" {
            constraints {
                controller(matches:/(login)|(logout)/)
            }
        }


        // Admin app
        "/admin"(view: "/admin/index")

        // User app
        "/public"(view: "/public/index")
//        "/"(view: "/public/index")

        "500"(view: '/error')
	}
}
