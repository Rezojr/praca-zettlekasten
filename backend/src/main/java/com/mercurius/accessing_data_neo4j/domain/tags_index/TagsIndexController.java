package com.mercurius.accessing_data_neo4j.domain.tags_index;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags-indexes")
@RequiredArgsConstructor
public class TagsIndexController {
    private final TagsIndexService tagsIndexService;

    @GetMapping
    public List<TagsIndex> getAll() {
        return tagsIndexService.getAll();
    }

    @GetMapping("/{tagsIndexId}")
    public TagsIndex get(@PathVariable Long tagsIndexId) {
        return tagsIndexService.getById(tagsIndexId);
    }

    @PostMapping
    public void create(@RequestBody TagsIndex tagsIndex) {
        tagsIndexService.create(tagsIndex);
    }

    @PutMapping("/{tagsIndexId}")
    public TagsIndex addTagsToIndex(@PathVariable Long tagsIndexId, @RequestBody List<Long> tagIds) {
        return tagsIndexService.addTagsToIndex(tagsIndexId, tagIds);
    }
}
