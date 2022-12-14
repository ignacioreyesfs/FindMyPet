package com.ireyes.findMyPet.model.post;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("found")
public class Found extends Post{
	@Enumerated(EnumType.STRING)
	private RelocationUrgency relocationUrgency;

	public RelocationUrgency getRelocationUrgency() {
		return relocationUrgency;
	}

	public void setRelocationUrgency(RelocationUrgency relocationUrgency) {
		this.relocationUrgency = relocationUrgency;
	}
	
}
