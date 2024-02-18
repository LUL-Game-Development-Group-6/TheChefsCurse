uniform float u_time;
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;


void main()
{
   vec4 color = texture2D(u_texture, v_texCoords);

   if (color.a > 0.5) {

       float fadeDuration = 0.5;
       float normalTime = min(u_time / fadeDuration, 1.0);
       float startAlpha = 0.9;
       float endAlpha = 0.0;

       float alpha = mix(startAlpha, endAlpha, normalTime);
       
       gl_FragColor = vec4(1, 0, 0, max(alpha, 0.0)); 
       
   } else {
    
       gl_FragColor = color;
   }
}
        