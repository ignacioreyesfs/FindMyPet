package com.ireyes.findMyPet.model.post;

import java.util.Map;
import java.util.function.Supplier;

public abstract class PostFactory {
	private static final Map<String, Supplier<Post>> factoryMap = Map.of(
			"search", Search::new,
			"found", Found::new);
	
	public static Post createPost(String type) {
		Supplier<Post> supplier = factoryMap.get(type);
		if(supplier == null) {
			throw new PostTypeNotFoundException(type);
		}
		
		return supplier.get();
	}
}
