package com.example.pokedex.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.pokedex.R;

import java.io.Serializable;

@Entity
public class Pokemon implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    protected String name;
    protected String height;
    protected String weight;
    protected String hp;
    protected String attack;
    protected String defence;
    protected String spAttack;
    protected String spDef;
    protected String speed;
    protected String type_1;
    protected String type_2;
    protected String ability_1;
    protected String ability_2;
    protected String ability_3;

    public Pokemon(int _id, String name, String height, String weight, String hp, String attack, String defence, String spAttack, String spDef, String speed, String type_1, String type_2, String ability_1, String ability_2, String ability_3) {
        this._id = _id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.hp = hp;
        this.attack = attack;
        this.defence = defence;
        this.spAttack = spAttack;
        this.spDef = spDef;
        this.speed = speed;
        this.type_1 = type_1;
        this.type_2 = type_2;
        this.ability_1 = ability_1;
        this.ability_2 = ability_2;
        this.ability_3 = ability_3;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getHp() {
        return hp;
    }

    public String getAttack() {
        return attack;
    }

    public String getDefence() {
        return defence;
    }

    public String getSpAttack() {
        return spAttack;
    }

    public String getSpDef() {
        return spDef;
    }

    public String getSpeed() {
        return speed;
    }

    public String getType_1() {
        return type_1;
    }

    public String getType_2() {
        return type_2;
    }

    public String getAbility_1() {
        return ability_1;
    }

    public String getAbility_2() {
        return ability_2;
    }

    public String getAbility_3() {
        return ability_3;
    }

    public int getTypeColor(String type){
        switch(type) {
            case "bug":
                return R.color.type_bug;
            case "dark":
                return R.color.type_dark;
            case "dragon":
                return R.color.type_dragon;
            case "electric":
                return R.color.type_electric;
            case "fairy":
                return R.color.type_fairy;
            case "fighting":
                return R.color.type_fighting;
            case "fire":
                return R.color.type_fire;
            case "flying":
                return R.color.type_flying;
            case "ghost":
                return R.color.type_ghost;
            case "grass":
                return R.color.type_grass;
            case "ground":
                return R.color.type_ground;
            case "ice":
                return R.color.type_ice;
            case "normal":
                return R.color.type_normal;
            case "poison":
                return R.color.type_poison;
            case "psychic":
                return R.color.type_psychic;
            case "rock":
                return R.color.type_rock;
            case "steel":
                return R.color.type_steel;
            case "water":
                return R.color.type_water;
            default:
                return 0;
        }
    }


    @Override
    public String toString() {
        return "Pokemon{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", hp='" + hp + '\'' +
                ", attack='" + attack + '\'' +
                ", defence='" + defence + '\'' +
                ", spAttack='" + spAttack + '\'' +
                ", spDef='" + spDef + '\'' +
                ", speed='" + speed + '\'' +
                ", type_1='" + type_1 + '\'' +
                ", type_2='" + type_2 + '\'' +
                ", ability_1='" + ability_1 + '\'' +
                ", ability_2='" + ability_2 + '\'' +
                ", ability_3='" + ability_3 + '\'' +
                '}';
    }

    public static class Type {
        private String name;

        public Type(String t){
            this.name = t;
        }

        public String getType() {
            return name;
        }

        public void setType(String type) {
            this.name = type;
        }

        private int getTypeColor(String type){
            switch(getType()) {
                case "bug":
                    return R.color.type_bug;
                case "dark":
                    return R.color.type_dark;
                case "dragon":
                    return R.color.type_dragon;
                case "electric":
                    return R.color.type_electric;
                case "fairy":
                    return R.color.type_fairy;
                case "fighting":
                    return R.color.type_fighting;
                case "fire":
                    return R.color.type_fire;
                case "flying":
                    return R.color.type_flying;
                case "ghost":
                    return R.color.type_ghost;
                case "grass":
                    return R.color.type_grass;
                case "ground":
                    return R.color.type_ground;
                case "ice":
                    return R.color.type_ice;
                case "normal":
                    return R.color.type_normal;
                case "poison":
                    return R.color.type_poison;
                case "psychic":
                    return R.color.type_psychic;
                case "rock":
                    return R.color.type_rock;
                case "steel":
                    return R.color.type_steel;
                case "water":
                    return R.color.type_water;
                default:
                    return 0;
            }
        }

        @Override
        public String toString() {
            return "Type{" +
                    "type='" + name + '\'' +
                    '}';
        }
    }

    public static class Stats{
        private String hp;
        private String attack;
        private String defense;
        private String spAttack;
        private String spDefense;
        private String speed;

        public Stats() {
        }

        public void setStat(String statString, String statNum){
            switch(statString){
                case "hp":
                    setHp(statNum);
                    break;
                case "attack":
                    setAttack(statNum);
                    break;
                case "defense":
                    setDefense(statNum);
                    break;
                case "special-attack":
                    setSpAttack(statNum);
                    break;
                case "special-defense":
                    setSpDefense(statNum);
                    break;
                case "speed":
                    setSpeed(statNum);
                    break;
            }
        }

        public String getHp() {
            return hp;
        }

        public void setHp(String hp) {
            this.hp = hp;
        }

        public String getAttack() {
            return attack;
        }

        public void setAttack(String attack) {
            this.attack = attack;
        }

        public String getDefense() {
            return defense;
        }

        public void setDefense(String defense) {
            this.defense = defense;
        }

        public String getSpAttack() {
            return spAttack;
        }

        public void setSpAttack(String spAttack) {
            this.spAttack = spAttack;
        }

        public String getSpDefense() {
            return spDefense;
        }

        public void setSpDefense(String spDefense) {
            this.spDefense = spDefense;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        @Override
        public String toString() {
            return "Stats{" +
                    "hp='" + hp + '\'' +
                    ", attack='" + attack + '\'' +
                    ", defense='" + defense + '\'' +
                    ", spAttack='" + spAttack + '\'' +
                    ", spDefense='" + spDefense + '\'' +
                    ", speed='" + speed + '\'' +
                    '}';
        }
    }

    public static class Ability{
        private String name;

        public Ability(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Abilites{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public static class Attributes{
        private String height;
        private String weight;

        public Attributes(String height, String weight) {
            this.height = height;
            this.weight = weight;
        }

        public String getHeight() {
            return height;
        }

        public String getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "Attributes{" +
                    "height='" + height + '\'' +
                    ", weight='" + weight + '\'' +
                    '}';
        }
    }

}
