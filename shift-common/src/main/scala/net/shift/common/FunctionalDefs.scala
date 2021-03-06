package net.shift
package common

trait Functor[F[_]] {
  def unit[A](a: A): F[A]
  def fmap[A, B](f: A => B): F[A] => F[B]
}

trait ApplicativeFunctor[F[_]] extends Functor[F] {
  def <*>[A, B](f: F[A => B]): F[A] => F[B]
}

trait Monad[M[_]] extends Functor[M] {
  def bind[A, B](f: A => M[B]): M[A] => M[B]
  def join[A](mma: M[M[A]]): M[A] = bind((ma: M[A]) => ma)(mma)
  def flatMap[A, B] = bind _
  def map[A, B](f: A => B): M[A] => M[B] = fmap(f)
}

trait Combinators[M[_]] {
  def >=>[A, B, C](f: A => M[B])(g: B => M[C]): A => M[C]
  def >|>[A, B](f: A => M[B])(g: A => M[B]): A => M[B]
}

trait Semigroup[A] {
  def append(a: A, b: A): A
}

trait State[S, +A] {
  import State._

  def apply(s: S): Option[(S, A)]

  def map[B](f: A => B): State[S, B] = state {
    apply(_) map { case (s, a) => (s, f(a)) }
  }

  def flatMap[B](f: A => State[S, B]): State[S, B] = state {
    apply(_) flatMap { case (st, a) => f(a)(st) }
  }

  def filter(f: A => Boolean): State[S, A] = state {
    apply(_) filter { case (s, a) => f(a) }
  }

  def withFilter(f: A => Boolean): State[S, A] = state {
    apply(_) filter { case (s, a) => f(a) }
  }

  def |[B >: A](other: State[S, B]): State[S, B] = state {
    x =>
      apply(x) match {
        case None => other apply x
        case s => s
      }
  }
}

trait Identity[M[_]] {
  def unit[A](a: A): M[A]
}

trait Bind[M[_]] {
  def bind[A, B](f: A => M[B]): M[A] => M[B]
}

trait Flat[F[_]] {
  def fmap[A, B](f: A => B): F[A] => F[B]
}

object Monad {
  def monad[M[_]](implicit id: Identity[M], flat: Flat[M], b: Bind[M]): Monad[M] = new Monad[M] {

    def unit[A](a: A): M[A] = id unit a

    def fmap[A, B](f: A => B): M[A] => M[B] = flat fmap f

    def bind[A, B](f: A => M[B]): M[A] => M[B] = b bind f
  }

}

object State {
  import Monad._
  
  implicit def stateMonad[S] = monad[({ type l[a] = State[S, a] })#l]

  implicit def stateBind[S]: Bind[({ type l[a] = State[S, a] })#l] = new Bind[({ type l[a] = State[S, a] })#l] {
    def bind[A, B](f: A => State[S, B]): State[S, A] => State[S, B] = s => s flatMap f
  }

  implicit def stateIdentity[S]: Identity[({ type l[a] = State[S, a] })#l] = new Identity[({ type l[a] = State[S, a] })#l] {
    def unit[A](a: A): State[S, A] = state {
      s => Some((s, a))
    }
  }

  implicit def stateFlat[S]: Flat[({ type l[a] = State[S, a] })#l] = new Flat[({ type l[a] = State[S, a] })#l] {
    def fmap[A, B](f: A => B): State[S, A] => State[S, B] = _ map f
  }

  def state[S, A](f: S => Option[(S, A)]): State[S, A] = new State[S, A] {
    def apply(s: S) = f(s)
  }

  def init[S] = state[S, S] {
    s => Some((s, s))
  }

  def initf[S](f: S => S) = state[S, S] {
    s => Some((f(s), f(s)))
  }

  def put[S] = state[S, Unit] {
    s => Some((s, ()))
  }

  def put[S, A](a: A) = state[S, A] {
    s => Some((s, a))
  }

  def modify[S](f: S => S) = state[S, Unit] {
    s => Some((f(s), ()))
  }

  def gets[S, A](f: S => A) = for (s <- init[S]) yield f(s)

}

