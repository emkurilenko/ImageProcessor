# ImageProcessor
> A tiny image processing software.
Implemented image processing algorithms like scaling/rotating, high- and low-pass filterings, de-noising, de-hazing, the Fourier Transform calculation, histogram equalization, etc. Written in Java.

## Installation & Execution
You should have JRE/JDK installed on your computer first.
Under the directory of bin, run in command line:
```sh
java -jar image_processor_en.jar
```
Or simply double click the jar file.

## Usage Example
English and Chinese language versions are provided.

Load an Image by clicking `open` under the `file` menu.
![](screenshots/open_file.png)

### Geometric Transformation

#### Scaling

1. Click `scale` under the `Geometric Transformation` menu.
2. Set the size you want to resize.
![](screenshots/scale.png)

#### Rotation

1. Click `rotate` under the `Geometric Transformation` menu.
2. Set the angle to rotate.
![](screenshots/rotate.png)

### Color Transformation

#### Gray Level Transformation (Grayscale)

The RGB image will first be converted to grayscale. 
Drag the bar to change to different gray level resolusions.
![](screenshots/gray_level_trans1.png)![](screenshots/gray_level_trans2.png)

#### Histogram

Calculate and show Histogram of the image.
![](screenshots/histogram.png)

Perform histogram equalization.
![](screenshots/histogram_equalization.png)
![](screenshots/equalized_histogram.png)

### Gray Image Operations

#### Intensity Transformations

#### Negative Image
#### Log Transformation

#### Low-pass Filtering (Smooth)

##### Linear

1. Box Filter
2. Weighted Average Filter

##### Non-linear

1. Median Filter

#### High-pass Filtering (Sharpen)

1. Laplacian Filter
2. Sobel Filter

#### Denoise
1. Gaussian or Uniform noise
   * Arithmetic mean filter
   * Geometric mean filter
2. Salt noise
   * Harmonic mean filter
   * Minimum filter
3. Pepper noise
   * Contraharmonic mean filter
   * Maximum filter
4. Salt and pepper noise
   * Median filter

## Meta

Zebin Xu - zebinxu7@gmail.com
