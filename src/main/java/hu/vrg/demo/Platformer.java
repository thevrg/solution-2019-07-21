package hu.vrg.demo;

import java.util.Optional;

public class Platformer {

    static class Tile {
        private Optional<Tile> next;
        private Optional<Tile> previous;
        private int position;

        public Tile(int position, Optional<Tile> previous) {
            this.previous = previous;
            this.position = position;
            this.next = Optional.empty();
        }

        public Optional<Tile> getNext() {
            return next;
        }

        public Optional<Tile> getPrevious() {
            return previous;
        }

        public int getPosition() {
            return position;
        }

        public void setPrevious(Tile previous) {
            this.previous = Optional.ofNullable(previous);
        }

        public void setPrevious(Optional<Tile> previous) {
            this.previous = previous;
        }

        public void setNext(Tile next) {
            this.next = Optional.ofNullable(next);
        }

        public void setNext(Optional<Tile> next) {
            this.next = next;
        }

        public void unlink() {
            this.previous.ifPresent(previousTile -> previousTile.setNext(next));
            this.next.ifPresent(nextTile -> nextTile.setPrevious(previous));
        }

        public Optional<Tile> jumpRight() {
            Optional<Tile> newPosition = next.flatMap(Tile::getNext); //jumping two tiles right
            if (newPosition.isPresent()) { //if it is possible
                this.unlink();
            }
            return newPosition;
        }

        public Optional<Tile> jumpLeft() {
            Optional<Tile> newPosition = previous.flatMap(Tile::getPrevious); //jumping two tiles right
            if (newPosition.isPresent()) { //if it is possible
                this.unlink();
            }
            return newPosition;
        }
    }

    Tile current;


    public Platformer(int n, int position) {
        Tile previous = new Tile(0, Optional.empty());
        if (position == 0) {
            current = previous;
        }
        for (int i = 1; i < n; i++) {
            Tile tile = new Tile(i, Optional.of(previous));
            if (position == i) {
                current = tile;
            }
            previous.setNext(tile);
            previous = tile;
        }

    }

    public void jumpLeft() {
        this.current = this.current.jumpLeft().get();
    }

    public void jumpRight() {
        this.current = this.current.jumpRight().get();
    }

    public int position() {
        return this.current.getPosition();
    }

    public static void main(String[] args) {
        Platformer platformer = new Platformer(10, 3);
        System.out.println(platformer.position());

        platformer.jumpLeft();
        System.out.println(platformer.position());

        platformer.jumpRight();
        System.out.println(platformer.position());
        platformer.jumpRight();
        System.out.println(platformer.position());
        platformer.jumpRight();
        System.out.println(platformer.position());
        platformer.jumpLeft();
        System.out.println(platformer.position());
    }
}
