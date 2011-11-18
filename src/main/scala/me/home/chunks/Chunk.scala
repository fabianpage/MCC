package me.home.chunks

trait Chunked {
  type A
  
  case class Location(val x: Int, val y: Int, val z: Int, subloc: Option[Location] = None) {
    // TODO: Add the capability to compare with different precision
  }

  class Chunk(val a: A, val id: Location, val subChunks: Option[Location => Chunk] = None)
}