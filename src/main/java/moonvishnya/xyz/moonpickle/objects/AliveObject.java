package moonvishnya.xyz.moonpickle.objects;


public interface AliveObject {

    /*
        каждый живой объект может двигаться,
        движение происходит за счет перемещения
        background'a
     */
    void move();

    /*
        прыжок -- изменение координаты Y
     */
    void jump();
}
