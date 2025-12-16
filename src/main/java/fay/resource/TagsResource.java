package fay.resource;

import fay.dto.tag.ResponseTag;
import fay.utils.TagUtils;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;

@Slf4j
@Path("tags")
public class TagsResource {

    @Inject
    TagUtils utils;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({"User"})
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<ResponseTag> getTagsForUser() {
        ResponseTag response = utils.fetchAllTagsForUser(jwt.getClaim("sub"));
        if (response == null || response.getTags().isEmpty()) {
            return RestResponse.notFound();
        }

        return RestResponse.ok(response);
    }

}
