package com.evolution.bootcamp.game_bot

import cats.Functor
import cats.effect.concurrent.Ref
import cats.implicits._

trait Repo[F[_], K, V] {
  def put(key: K, value: V): F[Unit]
  def delete(key: K): F[Unit]
  def get(key: K): F[Option[V]]
  def update(key: K, newValue: V): F[Unit]
}

final case class Repository[F[_] : Functor, K, V](
                                                   ref: Ref[F, Map[K, V]]
                                                 ) extends Repo[F, K, V] {
  override def put(key: K, value: V): F[Unit] =
    ref.update(_ + (key -> value))

  override def delete(key: K): F[Unit] =
    ref.update(_ - key)

  override def get(key: K): F[Option[V]] =
    ref.get.map(_.get(key))

  override def update(key: K, newValue: V): F[Unit] =
    ref.update(_.updated(key, newValue))
}
