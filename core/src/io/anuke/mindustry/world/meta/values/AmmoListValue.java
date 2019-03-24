package io.anuke.mindustry.world.meta.values;

import io.anuke.arc.Core;
import io.anuke.arc.collection.ObjectMap;
import io.anuke.arc.graphics.g2d.TextureRegion;
import io.anuke.arc.math.Mathf;
import io.anuke.arc.scene.ui.layout.Table;
import io.anuke.arc.util.Strings;
import io.anuke.mindustry.content.StatusEffects;
import io.anuke.mindustry.entities.bullet.BulletType;
import io.anuke.mindustry.game.UnlockableContent;
import io.anuke.mindustry.type.Item;
import io.anuke.mindustry.type.Item.Icon;
import io.anuke.mindustry.world.meta.StatValue;

import static io.anuke.mindustry.Vars.tilesize;

public class AmmoListValue<T extends UnlockableContent> implements StatValue{
    private final ObjectMap<T, BulletType> map;

    public AmmoListValue(ObjectMap<T, BulletType> map){
        this.map = map;
    }

    @Override
    public void display(Table table){

        table.row();
        for(T t : map.keys()){
            BulletType type = map.get(t);
            table.addImage(icon(t)).size(3*8).padRight(4).right().top();
            table.add(t.localizedName()).padRight(10).left().top();
            table.table("underline", bt -> {
                bt.left().defaults().padRight(3).left();
                bt.add(Core.bundle.format("bullet.damage", type.damage));

                if(type.splashDamage > 0){
                    sep(bt, Core.bundle.format("bullet.splashdamage", (int)type.splashDamage, Strings.toFixed(type.splashDamageRadius / tilesize, 1)));
                }

                if(!Mathf.isEqual(type.ammoMultiplier, 1f)) sep(bt, Core.bundle.format("bullet.multiplier", (int)type.ammoMultiplier));
                if(!Mathf.isEqual(type.reloadMultiplier, 1f)) sep(bt, Core.bundle.format("bullet.reload", Strings.toFixed(type.reloadMultiplier, 1)));

                if(type.knockback > 0){
                    sep(bt, Core.bundle.format("bullet.knockback", Strings.toFixed(type.knockback, 1)));
                }

                if((type.status == StatusEffects.burning || type.status == StatusEffects.melting) || type.incendAmount > 0){
                    sep(bt, "$bullet.incendiary");
                }

                if(type.status == StatusEffects.freezing){
                    sep(bt, "$bullet.freezing");
                }

                if(type.status == StatusEffects.tarred){
                    sep(bt, "$bullet.tarred");
                }

                if(type.homingPower > 0.01f){
                    sep(bt, "$bullet.homing");
                }

                if(type.lightining > 0){
                    sep(bt, "$bullet.shock");
                }

                if(type.fragBullet != null){
                    sep(bt, "$bullet.frag");
                }
            }).left().padTop(-9);
            table.row();
        }
    }

    void sep(Table table, String text){
        table.row();
        table.add(text);
    }

    TextureRegion icon(T t){
        if(t instanceof Item){
            return ((Item)t).icon(Icon.medium);
        }
        return t.getContentIcon();
    }
}
