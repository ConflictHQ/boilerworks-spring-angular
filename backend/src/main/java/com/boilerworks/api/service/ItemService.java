package com.boilerworks.api.service;

import com.boilerworks.api.dto.*;
import com.boilerworks.api.model.Category;
import com.boilerworks.api.model.Item;
import com.boilerworks.api.repository.CategoryRepository;
import com.boilerworks.api.repository.ItemRepository;
import com.boilerworks.api.security.BoilerworksUserDetails;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

  private final ItemRepository itemRepository;
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public List<ItemDto> findAll(String search) {
    List<Item> items;
    if (search != null && !search.isBlank()) {
      items = itemRepository.search(search);
    } else {
      items = itemRepository.findAll();
    }
    return items.stream().map(ItemDto::from).toList();
  }

  @Transactional(readOnly = true)
  public ItemDto findById(UUID id) {
    Item item =
        itemRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item not found"));
    return ItemDto.from(item);
  }

  public Item create(CreateItemRequest request) {
    if (itemRepository.existsBySku(request.getSku())) {
      throw new IllegalArgumentException("SKU already exists");
    }

    Item item = new Item();
    item.setName(request.getName());
    item.setSlug(SlugUtil.slugify(request.getName()));
    item.setDescription(request.getDescription());
    item.setPrice(request.getPrice());
    item.setSku(request.getSku());
    item.setActive(request.isActive());

    if (request.getCategoryId() != null) {
      Category category =
          categoryRepository
              .findById(request.getCategoryId())
              .orElseThrow(() -> new EntityNotFoundException("Category not found"));
      item.setCategory(category);
    }

    return itemRepository.save(item);
  }

  public Item update(UUID id, UpdateItemRequest request) {
    Item item =
        itemRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item not found"));

    if (request.getName() != null) {
      item.setName(request.getName());
      item.setSlug(SlugUtil.slugify(request.getName()));
    }
    if (request.getDescription() != null) {
      item.setDescription(request.getDescription());
    }
    if (request.getPrice() != null) {
      item.setPrice(request.getPrice());
    }
    if (request.getSku() != null) {
      item.setSku(request.getSku());
    }
    if (request.getActive() != null) {
      item.setActive(request.getActive());
    }
    if (request.getCategoryId() != null) {
      Category category =
          categoryRepository
              .findById(request.getCategoryId())
              .orElseThrow(() -> new EntityNotFoundException("Category not found"));
      item.setCategory(category);
    }

    return itemRepository.save(item);
  }

  public void softDelete(UUID id) {
    Item item =
        itemRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item not found"));

    UUID userId = getCurrentUserId();
    item.softDelete(userId);
    itemRepository.save(item);
  }

  private UUID getCurrentUserId() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof BoilerworksUserDetails userDetails) {
      return userDetails.getUserId();
    }
    return null;
  }
}
