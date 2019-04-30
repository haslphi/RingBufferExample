package at.jku.swtesting.modeltesting;

import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;

public class RingBufferModel implements FsmModel {
	private static final int CAPACITY = 5;
	private int size = 0;

	@Override
	public Object getState() {
		if(size==0) return "EMPTY";
		if(size== CAPACITY) return "FULL";
		return "FILLED";
	}

	@Override
	public void reset(boolean testing) {
		size = 0;
	}
	
	@Action
	public void enqueue() {
		size++;
	}
	public boolean enqueueGuard() {
		return size < CAPACITY;
	}
	
	@Action
	public void dequeue() {
		size--;
	}
	public boolean dequeueGuard() {
		return size > 0;
	}
	
	@Action
	public void dequeueFromEmptyBuffer() {
		//causes an error in SUT;
	}
	public boolean dequeueFromEmptyBufferGuard() {
		return size == 0;
	}

}
