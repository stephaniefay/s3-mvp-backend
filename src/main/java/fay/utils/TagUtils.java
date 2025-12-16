package fay.utils;

import fay.dto.tag.ResponseTag;
import fay.model.card.Tag;
import fay.repository.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
public class TagUtils {

    @Inject
    TagRepository tagRepository;

    public ResponseTag fetchAllTagsForUser(String userId) {
        List<Tag> tags = tagRepository.fetchAllTags(userId);

        ResponseTag response = new ResponseTag();
        for (fay.model.card.Tag tag : tags) {
            response.getTags().add(new fay.dto.tag.Tag(tag));
        }

        return response;
    }

}
