package com.mercurius.accessing_data_neo4j.domain.tags_index;

import com.mercurius.accessing_data_neo4j.domain.tag.Tag;
import com.mercurius.accessing_data_neo4j.domain.tag.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagsIndexService {
    private final TagsIndexRepository tagsIndexRepository;
    private final TagRepository tagRepository;

    public List<TagsIndex> getAll() {
        return tagsIndexRepository.findAll();
    }

    public TagsIndex getById(Long id) {
        return tagsIndexRepository.findById(id).orElse(null);
    }

    public void create(TagsIndex tagsIndex) {
        tagsIndexRepository.save(tagsIndex);
    }

    @Transactional
    public TagsIndex addTagsToIndex(Long tagsIndexId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return null;
        }

        TagsIndex tagsIndex = tagsIndexRepository.findById(tagsIndexId)
                .orElseThrow(() -> new RuntimeException("TagsIndex not found"));

        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new RuntimeException("Some tags not found");
        }
        tagsIndex.addToIndex(tags);
        return tagsIndex;
    }

}
