          __   __  ____  ____  ____
         /  \\/  \/  _ \/  _ )/  _ \
         \       /   __/  _  \   __/
          \__\__/\____/\_____/__/ ____  ___
                / _/ /    \    \ /  _ \/ _/
               /  \_/   / /   \ \   __/  \__
               \____/____/\_____/_____/____/v0.1

Description:
============

WEBP codec: library to encode and decode images in WebP format. This package
containes command line tools 'cwebp' and 'dwebp' as well as static library
that can be used in other programs to add WebP support.

See http://code.google.com/speed/webp

Latest sources are available from http://www.webmproject.org/code/


It is released under the same license as the WebM project.
See http://www.webmproject.org/license/software/ or the
file "COPYING" file for details. An additional intellectual
property rights grant can be found in the file PATENTS.

Content of the package:
=======================

cwebp.exe - tool to encode an image file (e.g. JPEG or PNG) into WebP.
dwebp.exe - tool to decode a WebP file into e.g. PNG.
test.webp - a sample WebP file.
test_ref.ppm - the test.webp file decodec into the PPM format.
dev\Include - include files needed if you want to use libwebp in your programs.
dev\Lib - static library for libwebp that can be included in your programs.


Encoding tool:
==============

The package contains tools for encoding (cwebp) and decoding (dwebp) images.

The easiest use should look like:
  cwebp input.png -q 80 -o output.webp
which will convert the input (JPEG, TIFF, GIF, PNG, BMP, JPEG XR or ICO) file
to a WebP one using a quality factor of 80 on a 0->100 scale (0 being the
lowest quality, 100 being the best. Default value is 75).

A longer list of options is available using the -longhelp command line flag:

> cwebp -longhelp
Usage:
 cwebp [-preset <...>] [options] in_file [-o out_file]

If input size (-s) for an image is not specified, it is assumed to be a either
  PNG or JPEG format file.
options:
  -h / -help  ............ short help
  -H / -longhelp  ........ long help
  -q <float> ............. quality factor (0:small..100:big)
  -preset <string> ....... Preset setting, one of:
                            default, photo, picture,
                            drawing, icon, text
     -preset must come first, as it overwrites other parameters.
  -m <int> ............... compression method (0=fast, 6=slowest)
  -segments <int> ........ number of segments to use (1..4)

  -s <int> <int> ......... Input size (width x height) for YUV
  -sns <int> ............. Spatial Noise Shaping (0:off, 100:max)
  -f <int> ............... filter strength (0=off..100)
  -sharpness <int> ....... filter sharpness (0:most .. 7:least sharp)
  -strong ................ use strong filter instead of simple.
  -pass <int> ............ analysis pass number (1..10)
  -partitions <int> ...... number of partitions to use (0..3)
  -crop <x> <y> <w> <h> .. crop picture with the given rectangle
  -map <int> ............. print map of extra info.
  -d <file.pgm> .......... dump the compressed output (PGM file).

  -short ................. condense printed message
  -quiet ................. don't print anything.
  -v ..................... verbose, e.g. print encoding/decoding times

Experimental Options:
  -size <int> ............ Target size (in bytes)
  -psnr <float> .......... Target PSNR (in dB. typically: 42)
  -af <int> .............. adjust filter strength (0=off, 1=on)
  -pre <int> ............. pre-processing filter


The main options you might want to try in order to further tune the
visual quality are:
 -preset
 -sns
 -f
 -m

Namely:
  * 'preset' will set up a default encoding configuration targetting a
     particular type of input. It should appear first in the list of options,
     so that subsequent options can take effect on top of this preset.
     Default value is 'default'.
  * 'sns' will progressively turn on (when going from 0 to 100) some additional
     visual optimizations (like: segmentation map re-enforcement). This option
     will balance the bit allocation differently. It tries to take bits from the
     "easy" parts of the picture and use them in the "difficult" ones instead.
     Usually, raising the sns value (at fixed -q value) leads to larger files,
     but with better quality.
     Typical value is around '75'.
  * 'f' option directly links to the filtering strength used by the codec's
     in-loop processing. The higher, the smoother will highly-compressed area
     look. This is particularly useful when aiming at very small files.
     Typical values are around 20-30. Note that you must be using profile 0 or 1
     to have the in-loop filtering activated.
  * 'm' controls the trade-off between encoding speed and quality. Default is 4.
     You can try -m 5 or -m 6 to explore more (time-consuming) encoding
     possibilities. A lower value will result in faster encoding at the expense
     of quality.

Decoding tool:
==============

The sample decoding program dwebp.exe will take a .webp file and decode it to
a PNG image file (amongst other formats). You can verify the file test.webp
decodes to exactly the same as test_ref.ppm by using:

 cd examples
 ./dwebp test.webp -ppm -o test.ppm
 diff test.ppm test_ref.ppm


Encoding API:
=============

The library can also be used in your programs to add support for WebP. The
package containes a precompiled static library in release and debug builds.
It has been compiled with Visual Studio 2010 with static runtime library.
It has not been checked if works in other configurations.

The main encoding functions are available in the header
dev\Include\webp\encode.h
The ready-to-use ones are:
size_t WebPEncodeRGB(const uint8_t* rgb, int width, int height, int stride,
                     float quality_factor, uint8_t** output);
size_t WebPEncodeBGR(const uint8_t* bgr, int width, int height, int stride,
                     float quality_factor, uint8_t** output);
size_t WebPEncodeRGBA(const uint8_t* rgba, int width, int height, int stride,
                      float quality_factor, uint8_t** output);
size_t WebPEncodeBGRA(const uint8_t* bgra, int width, int height, int stride,
                      float quality_factor, uint8_t** output);

They will convert raw RGB samples to a WebP data. The only control supplied
is the quality factor.


A more advanced API is based on the WebPConfig and WebPPicture structures.

WebPConfig contains the encoding settings and is not tied a to a particular
picture.
WebPPicture contains input data, on which some WebPConfig will be used for
compression.
The encoding flow looks like:

-------------------------------------- BEGIN PSEUDO EXAMPLE

#include <webp/encode.h>

  // Setup a config, starting form a preset and tuning some additional
  // parameters
  WebPConfig config;
  if (!WebPConfigPreset(&config, WEBP_PRESET_PHOTO, quality_factor))
    return 0;   // version error
  }
  // ... additional tuning
  config.sns_strength = 90;
  config.filter_sharpness = 6;
  config_error = WebPValidateConfig(&config);  // not mandartory, but useful

  // Setup the input data
  WebPPicture pic;
  if (!WebPPictureInit(&pic)) {
    return 0;  // version error
  }
  pic.width = width;
  pic.height = height;
  // allocated picture of dimension width x height
  if (!WebPPictureAllocate(&pic)) {
    return 0;   // memory error
  }
  // add that point, 'pic' has been initialized as a container,
  // and can receive the Y/U/V samples.
  // Alternatively, one could use ready-made import functions like
  // WebPPictureImportRGB(), which will take care of memory allocation.
  // In any case, past this point, one will have to call
  // WebPPictureFree(&pic) to reclaim memory.


  // Set up a byte-output write method. WebPMemoryWriter, for instance.
  WebPMemoryWriter wrt;
  pic.writer = MyFileWriter;
  pic.custom_ptr = my_opaque_structure_to_make_MyFileWriter_work;
  // initialize 'wrt' here...

  // Compress!
  int ok = WebPEncode(&config, &pic);   // ok = 0 => error occured!
  WebPPictureFree(&pic);  // must be called independently of the 'ok' result.

  // output data should have been handled by the writer at that point.

-------------------------------------- END PSEUDO EXAMPLE


Decoding API:
=============

This is mainly just one function to call:

#include "webp/decode.h"
uint8_t* WebPDecodeRGB(const uint8_t* data, uint32_t data_size,
                       int *width, int *height);

Please have a look at the file src/webp/decode.h for the details.
There are variants for decoding in BGR/RGBA/BGRA order, along with decoding to
raw Y'CbCr samples. One can also decode the image directly into a pre-allocated
buffer.

To detect a WebP file and gather picture's dimensions, the function:
  int WebPGetInfo(const uint8_t* data, uint32_t data_size,
                  int *width, int *height);
is supplied. No decoding is involved when using it.

A lower-level API is available from the header file <webp/decode_vp8.h>

Bugs:
=====

Please report all bugs to our issue tracker:
    http://code.google.com/p/webp/issues
Patches welcome! See this page to get started:
    http://www.webmproject.org/code/contribute/submitting-patches/

Discuss:
========

Email: webp-discuss@webmproject.org

