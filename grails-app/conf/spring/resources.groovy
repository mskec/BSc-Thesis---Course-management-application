import org.springframework.web.multipart.commons.CommonsMultipartResolver

beans = {

    multipartResolver(CommonsMultipartResolver) {
        maxUploadSize = 1024 * 1024 * 10
    }

}
