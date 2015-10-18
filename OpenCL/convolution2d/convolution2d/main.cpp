#define __CL_ENABLE_EXCEPTIONS
#include <CL/cl.h>
#include "cl.hpp"

#include <vector>
#include <fstream>
#include <iostream>
#include <iomanip>
#include <iterator>
#include <math.h>

int main()
{
	std::string infilename = "input.txt";
	std::string outfilename = "output.txt";
	std::ifstream infile(infilename);
	std::ofstream outfile(outfilename);

	if (!infile) {
		std::cout << "Failed to open file " << infilename;
		getchar();
		return 1;
	}

	int n;
	int m;
	infile >> n >> m;

	size_t const block_size = 16;
	size_t const N = ((n - 1) / block_size + 1) * block_size;
	size_t const M = m;

	size_t const matrix_size = N * N;
	size_t const mask_size = M * M;

	std::vector<float> input(matrix_size, 0);
	std::vector<float> mask(mask_size, 0);
	std::vector<float> output(matrix_size, 0);

	float a;
	for (size_t i = 0; i < n; ++i) {
		for (size_t j = 0; j < n; ++j) {
			infile >> a;
			input[i*N + j] = a;
		}
	}

	for (size_t i = 0; i < M; ++i) {
		for (size_t j = 0; j < M; ++j) {
			infile >> a;
			mask[i*M + j] = a;
		}
	}


	std::vector<cl::Platform> platforms;
	std::vector<cl::Device> devices;
	std::vector<cl::Kernel> kernels;

	try {

		// create platform
		cl::Platform::get(&platforms);
		platforms[0].getDevices(CL_DEVICE_TYPE_GPU, &devices);

		// create context
		cl::Context context(devices);

		// create command queue
		cl::CommandQueue queue(context, devices[0]);

		// load opencl source
		std::ifstream cl_file("convolution2d.cl");
		std::string cl_string(std::istreambuf_iterator<char>(cl_file), (std::istreambuf_iterator<char>()));
		cl::Program::Sources source(1, std::make_pair(cl_string.c_str(),
			cl_string.length() + 1));

		// create program
		cl::Program program(context, source);

		// compile opencl source
		program.build(devices, "-D BLOCK_SIZE=16");

	 
      // allocate device buffer to hold message
		cl::Buffer dev_input(context, CL_MEM_READ_ONLY, sizeof(float) * matrix_size);
	  cl::Buffer dev_mask(context, CL_MEM_READ_ONLY, sizeof(float) * mask_size);
	  cl::Buffer dev_output(context, CL_MEM_WRITE_ONLY, sizeof(float) * matrix_size);

      // copy from cpu to gpu
	  queue.enqueueWriteBuffer(dev_input, CL_TRUE, 0, sizeof(float) * matrix_size, &input[0]);
	  queue.enqueueWriteBuffer(dev_mask, CL_TRUE, 0, sizeof(float)* mask_size, &mask[0]);

      // load named kernel from opencl source
      queue.finish();
      cl::Kernel kernel_gmem(program, "gpu_convolution_gmem");
	  cl::KernelFunctor convolution_gmem(kernel_gmem, queue, cl::NullRange, cl::NDRange(N, N), cl::NDRange(block_size, block_size));
      convolution_gmem(dev_input, dev_mask, dev_output, M, N);

	  queue.enqueueReadBuffer(dev_output, CL_TRUE, 0, sizeof(float) * matrix_size, &output[0]);
	  
	  outfile << std::fixed << std::setprecision(2);
	  for (size_t i = 0; i < n; ++i) {
		  for (size_t j = 0; j < n; ++j) {
			  outfile << output[i*N + j] << " ";
		  }
		  outfile << "\n";
	  }
   }
   catch (cl::Error e)
   {
      std::cout << std::endl << e.what() << " : " << e.err() << std::endl;
   }

   std::cout << "end\n";
   getchar();
   return 0;
}