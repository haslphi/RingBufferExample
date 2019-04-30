package at.jku.swtesting.modeltesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import at.jku.swtesting.RingBuffer;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;

public class RingBufferModelWithAdapter implements FsmModel {
	private static final int CAPACITY = 5;
	private int size = 0;
	private boolean maxExceeded = true;
	private RingBuffer<Integer> rb = new RingBuffer<>(CAPACITY);
	private int[] oracleArray = new int[] {0, 1, 2, 3, 4};
	// handle position of oracleArray to enqueue elements that are predicatble for dequeue
	private int oraclePos = -1;
	// handle first element to make result of dequeue assertable
	private int firstElement = -1;

	@Override
	public Object getState() {
		if(size==0) return "EMPTY";
		if(size== CAPACITY) return "FULL";
		return "FILLED";
	}

	@Override
	public void reset(boolean testing) {
		size = 0;
		oraclePos = -1;
		firstElement = -1;
		maxExceeded = false;
		rb = new RingBuffer<>(CAPACITY);
	}
	
	@Action
	public void enqueue() {
		if(size == 0 || size == CAPACITY) {
			// first enque or new cycle -> set initalize firsElement variable
			if(maxExceeded) {
				firstElement++;
				firstElement %= CAPACITY;
			}
			firstElement = 0;
		}
		
		if(size < CAPACITY) {
			size++;
		}
		
		maxExceeded = size == CAPACITY;
		
		// handle position of oracleArray to enqueue elements that are predicatble for dequeue
		oraclePos++;
		oraclePos %= CAPACITY;
		
		rb.enqueue(oracleArray[oraclePos]);
	}
	public boolean enqueueGuard() {
		// as this is a RingBuff (Ring!!), there is no hard limit,
		// as it starts from the beginning to override elements
		return true;
	}
	
	@Action
	public void enqueueLimitedToCapacity() {
		if(size < CAPACITY) {
			size++;
		}
		if(size == 1 || size == CAPACITY) {
			// first enque or new cycle -> set initalize firsElement variable
			firstElement = 0;
		}
		
		oraclePos++;
		// not necessary, as the guard function is preventing from cycling the buffer
		//		oraclePos %= CAPACITY;
		
		rb.enqueue(oracleArray[oraclePos]);
	}
	public boolean enqueueLimitedToCapacityGuard() {
		return size < CAPACITY;
	}
	
	@Action
	public void dequeue() {
		size--;
		maxExceeded = false;
		
		if(size == 0) {
			oraclePos = -1;
		}
		int result = rb.dequeue();
		assertEquals(oracleArray[firstElement], result);
		firstElement++;
		firstElement %= CAPACITY;
	}
	public boolean dequeueGuard() {
		return size > 0;
	}
	
	@Action
	public void dequeueFromEmptyBuffer() {
		boolean exceptionThrown = false;
		try {
			rb.dequeue();
		} catch (IllegalStateException e) {
			assertEquals("Empty ring buffer.", e.getMessage());
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	public boolean dequeueFromEmptyBufferGuard() {
		return size == 0;
	}
	
	@Action
	public void peek() {
		int result = rb.peek();
	}
	public boolean peekGuard() {
		return size > 0;
	}
	
	@Action
	public void peekFromEmptyBuffer() {
		boolean exceptionThrown = false;
		try {
			rb.peek();
		} catch (IllegalStateException e) {
			assertEquals("Empty ring buffer.", e.getMessage());
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	public boolean peekFromEmptyBufferGuard() {
		return size == 0;
	}

}
