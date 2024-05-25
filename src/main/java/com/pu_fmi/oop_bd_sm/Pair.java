/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pu_fmi.oop_bd_sm;

/**
 *
 * @author amtis
 * @param <TShown> will be used for to string
 * @param <THidden>
 */
public class Pair<TShown, THidden> {

    public TShown Shown;
    public THidden Hidden;

    public Pair(TShown shown, THidden hidden) {
        Shown = shown;
        Hidden = hidden;
    }

    @Override
    public String toString() {
        return this.Shown.toString();
    }
}
