package me.home.chunks

import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers

class ChunkTest extends FlatSpec with MustMatchers with Chunked {
	override type A = Int
	"A chunk" must "have an identity which is its location" in {
	  val c = new Chunk(0, Location(0,0,0))
	  c.id must be (Location(0,0,0))
	}
	
	it must "be creatable by the millions" in {
	  val max = 16
	  val locs = for {
		x <- 0 until max
		y <- 0 until max
		z <- 0 until max
	  	} yield Location(x,y,z)
	  
	  locs.zipWithIndex.map { t => new Chunk(t._2, t._1) }
	}
}