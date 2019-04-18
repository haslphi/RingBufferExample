package at.jku.swtesting;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class RingBufferFullCoverageTest {

	@Test
	public void testCapacity() {
		int capacity = 5;
		RingBuffer<Object> ringBuffer = new RingBuffer<>(capacity);
		
		assertEquals(capacity, ringBuffer.capacity());
	}
	
	@Test
	public void testCapacityMin() {
		boolean hasException = false;

		try {
			RingBuffer<Object> ringBuffer = new RingBuffer<>(-1);
		} catch (IllegalArgumentException e) {
			hasException = true;	// correctly thrown exception
		}
		assertTrue(hasException);
		hasException = false;	// reset exception

		try {
			RingBuffer<Object> ringBuffer = new RingBuffer<>(0);
		} catch (IllegalArgumentException e) {
			hasException = true;	// correctly thrown exception
		}
		assertTrue(hasException);
		hasException = false;	// reset exception

		int capacity = 1;
		RingBuffer<Object> ringBuffer = new RingBuffer<>(capacity);
		
		assertEquals(capacity, ringBuffer.capacity());
	}

	@Test
	public void testSize() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);
		ringBuffer.enqueue("item1");
		ringBuffer.enqueue("item2");
		
		// size = 2
		assertEquals(2, ringBuffer.size());
		
		ringBuffer.enqueue("item3");
		
		// size = 3
		assertEquals(3, ringBuffer.size());
		
		ringBuffer.dequeue();
		
		// size = 2
		assertEquals(2, ringBuffer.size());
		
		ringBuffer.enqueue("item3");
		ringBuffer.enqueue("item4");
		ringBuffer.enqueue("item5");
		ringBuffer.enqueue("item6");
		ringBuffer.enqueue("item7");
		ringBuffer.enqueue("item8");

		// size = 5; maximum size
		assertEquals(5, ringBuffer.size());

		ringBuffer.dequeue();
		
		// size = 4
		assertEquals(4, ringBuffer.size());

		ringBuffer.dequeue();
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		
		// size = 0; minimum size
		assertEquals(0, ringBuffer.size());
	}

	@Test
	public void testEmpty() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);
		
		// empty
		assertTrue(ringBuffer.isEmpty());	

		ringBuffer.enqueue("item1");
		ringBuffer.enqueue("item1");
		ringBuffer.enqueue("item1");
		ringBuffer.enqueue("item1");
		ringBuffer.enqueue("item1");
		ringBuffer.enqueue("item1");
		ringBuffer.enqueue("item1");
		ringBuffer.enqueue("item1");
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		
		// empty
		assertTrue(ringBuffer.isEmpty());
	}

	@Test
	public void testIsFullForNonFullBuffer() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);
		
		// empty
		assertFalse(ringBuffer.isFull());
		
		// partially filled
		ringBuffer.enqueue("item1");
		assertFalse(ringBuffer.isFull());
		
		// partially filled
		ringBuffer.dequeue();
		ringBuffer.enqueue("item1");
		assertFalse(ringBuffer.isFull());

		// partially filled
		ringBuffer.enqueue("item2");
		ringBuffer.enqueue("item3");
		ringBuffer.enqueue("item4");
		ringBuffer.enqueue("item5");
		ringBuffer.dequeue();
		assertFalse(ringBuffer.isFull());
		
		// empty
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		assertFalse(ringBuffer.isFull());
	}
	
	@Test
	public void testIsFull() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);

		ringBuffer.enqueue("item1");
		ringBuffer.enqueue("item2");
		ringBuffer.enqueue("item3");
		ringBuffer.enqueue("item4");
		ringBuffer.enqueue("item5");
		
		// full
		assertTrue(ringBuffer.isFull());
		
		ringBuffer.dequeue();
		ringBuffer.enqueue("item5");
		
		// full
		assertTrue(ringBuffer.isFull());

		ringBuffer.enqueue("item6");
		ringBuffer.enqueue("item7");
		ringBuffer.enqueue("item8");
		
		// full
		assertTrue(ringBuffer.isFull());
	}
	
	@Test
	public void testIterator() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);
		boolean hasException = false;

		String[] items = new String[] { "item1", "item2", "item3", "item4", "item5" };
		ringBuffer.enqueue(items[0]);
		ringBuffer.enqueue(items[1]);
		ringBuffer.enqueue(items[2]);
		ringBuffer.enqueue(items[3]);
		ringBuffer.enqueue(items[4]);
		
		Iterator<String> iterator = ringBuffer.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			assertEquals(items[i++], iterator.next());
		}
		
		try {
			iterator.remove();
		} catch (UnsupportedOperationException e) {
			hasException = true;	// remove is optionally and not supported
		}
		assertTrue(hasException);
		hasException = false;	// reset exception

		assertFalse(iterator.hasNext());

		try {
			iterator.next();
		} catch (NoSuchElementException e) {
			hasException = true;	// iterator is at its end, no more elements
		}
		assertTrue(hasException);
		hasException = false;	// reset exception

	}
	
	@Test
	public void testEnqueue() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);

		String[] items = new String[] { "item1", "item2", "item3", "item4", "item5", "overwrittenItem" };
		// this item will be overwritten when limit is reached
		ringBuffer.enqueue(items[5]);
		ringBuffer.enqueue(items[1]);
		ringBuffer.enqueue(items[2]);
		ringBuffer.enqueue(items[3]);
		ringBuffer.enqueue(items[4]);
		ringBuffer.enqueue(items[0]);
		
		Iterator<String> iterator = ringBuffer.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			assertEquals(items[i++], iterator.next());
		}
	}
	
	@Test
	public void testDequeue() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);
		boolean hasException = false;

		try {
			ringBuffer.dequeue();
		} catch (IllegalStateException e) {
			hasException = true;	// the buffer is empty, so an exception is thrown when trying to dequeue
		}
		assertTrue(hasException);
		hasException = false;	// reset exception

		String[] items = new String[] { "item1", "item2", "item3", "item4", "item5" };
		ringBuffer.enqueue(items[0]);
		ringBuffer.enqueue(items[1]);
		ringBuffer.enqueue(items[2]);
		ringBuffer.enqueue(items[3]);
		ringBuffer.enqueue(items[4]);
		
		assertEquals(items[0], ringBuffer.dequeue());
		assertEquals(items[1], ringBuffer.dequeue());
		assertEquals(items[2], ringBuffer.dequeue());
		assertEquals(items[3], ringBuffer.dequeue());
		assertEquals(items[4], ringBuffer.dequeue());
	}

	@Test
	public void testCircle() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);
		boolean hasException = false;

		try {
			ringBuffer.dequeue();
		} catch (IllegalStateException e) {
			hasException = true;	// the buffer is empty, so an exception is thrown when trying to dequeue
		}
		assertTrue(hasException);
		hasException = false;	// reset exception

		String[] items = new String[] { "1", "2", "3", "4", "5", "6", "7", "A", "B" };

		ringBuffer.enqueue(items[0]);
		ringBuffer.enqueue(items[1]);
		ringBuffer.enqueue(items[2]);
		// Current Buffer 1 - 2 - 3 - _ - _

		assertEquals(items[0], ringBuffer.dequeue());
		assertEquals(items[1], ringBuffer.dequeue());
		// Current Buffer _ - _ - 3 - _ - _

		ringBuffer.enqueue(items[3]);
		ringBuffer.enqueue(items[4]);
		ringBuffer.enqueue(items[5]);
		ringBuffer.enqueue(items[6]);
		// Current Buffer 6 - 7 - 3 - 4 - 5
		ringBuffer.enqueue(items[7]);
		// Current Buffer 6 - 7 - A - 4 - 5

		// first should be elem with value "4"
		assertEquals(items[3], ringBuffer.dequeue());
		// first should be elem with value "5"
		assertEquals(items[4], ringBuffer.dequeue());
	}
	
	@Test
	public void testTypes() {
		RingBuffer<Integer> ringBufferInt = new RingBuffer<>(2);
		ringBufferInt.enqueue(1);
		ringBufferInt.enqueue(new Integer(2));
		assertEquals(new Integer(1), ringBufferInt.dequeue());
		assertEquals(new Integer(2), ringBufferInt.dequeue());

		RingBuffer<Object> ringBufferObj = new RingBuffer<>(2);
		ringBufferObj.enqueue(1);
		ringBufferObj.enqueue("string");
		assertEquals(1, ringBufferObj.dequeue());
		assertEquals("string", ringBufferObj.dequeue());
	}

	@Test
	public void testEmptyPeek() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(1);
		boolean hasException = false;

		try {
			ringBuffer.peek();
		} catch (IllegalStateException e) {
			hasException = true;	// the buffer is empty, so an exception is thrown when trying to peek
		}
		assertTrue(hasException);
		hasException = false;	// reset exception
	}

	@Test
	public void testPeek() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);
		ringBuffer.enqueue("1");	// first elem
		ringBuffer.enqueue("2");	// second elem
		ringBuffer.enqueue("3");	// third elem

		assertEquals("1", ringBuffer.peek());
		// test second time -> peek() must not remove first element
		assertEquals("1", ringBuffer.peek());
	}

	@Test
	public void testPeekAfterEnqueue() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);
		ringBuffer.enqueue("1");	// first elem
		ringBuffer.enqueue("2");	// second elem
		ringBuffer.enqueue("3");	// third elem

		// peek() result must not be affected if buffer is not full
		ringBuffer.enqueue("4");	// fourth elem
		assertEquals("1", ringBuffer.peek());
	}

	@Test
	public void testPeekAfterDequeue() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);
		ringBuffer.enqueue("1");	// first elem
		ringBuffer.enqueue("2");	// second elem
		ringBuffer.enqueue("3");	// third elem

		ringBuffer.dequeue();	// "2" is new first elem
		assertNotEquals("1", ringBuffer.peek());
		assertEquals("2", ringBuffer.peek());
	}

	@Test
	public void testPeekFullBuffer() {
		RingBuffer<String> ringBuffer = new RingBuffer<>(5);
		ringBuffer.enqueue("1");
		ringBuffer.enqueue("2");
		ringBuffer.enqueue("3");
		ringBuffer.enqueue("4");
		ringBuffer.enqueue("5");
		// Current Buffer 1 - 2 - 3 - 4 - 5

		ringBuffer.enqueue("A");
		ringBuffer.enqueue("B");
		// Current Buffer A - B - 3 - 4 - 5
		// "3" has to be the new first elem
		assertEquals("3", ringBuffer.peek());
	}
}
