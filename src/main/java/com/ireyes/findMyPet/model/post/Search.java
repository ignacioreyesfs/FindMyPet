package com.ireyes.findMyPet.model.post;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("search")
public class Search extends Post{

}
