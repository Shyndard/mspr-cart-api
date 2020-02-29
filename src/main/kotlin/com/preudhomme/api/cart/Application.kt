package com.preudhomme.api.cart

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.info.License
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.ws.rs.core.Application


@OpenAPIDefinition(info = Info(title = "Cart API", version = "1.0.0",  license = License(name = "Pakur & CO", url = "http://www.apache.org/licenses/LICENSE-2.0.html")))
class ExampleApiApplication : Application()