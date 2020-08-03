package xyz.fourthirdskiwidrive.dungeonshud.map;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import xyz.fourthirdskiwidrive.dungeonshud.ChunkSlice;
import xyz.fourthirdskiwidrive.dungeonshud.ChunkSliceManager;
import xyz.fourthirdskiwidrive.dungeonshud.util.RenderUtil;
import xyz.fourthirdskiwidrive.dungeonshud.util.Texture;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class MapTexture extends Texture {

    public MapTexture(int textureWidth, int textureHeight) {
        super(textureWidth, textureHeight, 0x00000000, GL11.GL_LINEAR, GL11.GL_LINEAR, GL11.GL_REPEAT);
    }

    public void updateTextureFromCSM(ChunkSliceManager csm) {
        float xscale = Math.round((float)this.w / (csm.xNeeded * 32));
        float zscale = Math.round((float)this.h / (csm.yNeeded * 32));
        for (int i = 0; i < csm.xNeeded; ++i) {
            for (int j = 0; j < csm.yNeeded; ++j) {
                BlockState[][] blocks = csm.slices.get(i).get(j).blocks;

                //if(chunkSlices.get(i).get(j).isEmpty()) continue;

                //Draw the map
                for (int k = 0; k < blocks.length; ++k) {
                    for (int l = 0; l < blocks[k].length; ++l) {
                        boolean isAir             = blocks[k][l].isAir();
                        boolean isWitherDoorBlock = blocks[k][l].isOf(Blocks.COAL_BLOCK);
                        boolean isBloodDoorBlock  = blocks[k][l].isOf(Blocks.RED_TERRACOTTA);


                        if(isAir) {
                            this.fillRect((int)((16*i+k) * xscale), (int)((16*j+l) * zscale), (int)xscale, (int)zscale, 0x00000000);
                        } else if (isWitherDoorBlock) {
                            this.fillRect((int)((16*i+k) * xscale), (int)((16*j+l) * zscale), (int)xscale, (int)zscale, Color.BLACK.getRGB() & 0x77ffffff);
                        } else if (isBloodDoorBlock) {
                            this.fillRect((int)((16*i+k) * xscale), (int)((16*j+l) * zscale), (int)xscale, (int)zscale, Color.RED.getRGB() & 0x77ffffff);
                        } else {
                            this.fillRect((int)((16*i+k) * xscale), (int)((16*j+l) * zscale), (int)xscale, (int)zscale, Color.GRAY.getRGB() & 0x77ffffff);
                        }
                    }
                }
            }
        }
    }

    public void draw(double x, double y, int imgX, int imgY, int width, int height, float scale) {
        if (width <= 0 || height <= 0) return;

        float u1 = imgX / (float)this.w;
        float v1 = imgY / (float)this.h;
        float u2 = (imgX + width) / (float)this.w;
        float v2 = (imgY + height) / (float)this.h;

        double scW = (double) width / scale;
        double scH = (double) height / scale;

        this.drawTexture(x, y, scW, scH, u1, v1, u2, v2);
    }

    public void drawTexture(double x, double y, double width, double height, float u1, float v1, float u2, float v2) {
        this.upload();
        this.bind();

        BufferBuilder bb = Tessellator.getInstance().getBuffer();
        bb.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);
        RenderUtil.addQuad(bb, x, y, width, height, u1, v1, u2, v2);
        //bb.end();
        Tessellator.getInstance().draw();
    }
}
