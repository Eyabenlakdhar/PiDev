<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\Security\Core\Exception\BadCredentialsException;
use App\Form\LoginType;
use App\Form\RegistrationType;
use App\Repository\UserRepository;
use App\Entity\User;

class SecurityController extends AbstractController
{
    #[Route('/',name:'app_home')]
    public function home(): Response
    {
        return $this->render('front.html.twig');
    }
    #[Route('/login', name: 'app_login', methods:["GET", "POST"])]
    public function login(Request $request, UserPasswordEncoderInterface $passwordEncoder, Security $security, UserRepository $userRepository ): Response
    {
        if($security->isGranted('IS_AUTHENTICATED_FULLY')){
            return $this->redirectToRoute('app_home');
        }
        $form = $this->createForm(LoginType::class);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $formData = $form->getData();
            $user = $userRepository->findOneBy(['email' => $formData['email']]);
            if(!$user){
                throw new BadCredentialsException('User not found.');
            }
            if(!$passwordEncoder->isPasswordValid($user, $formData['password'])){
                throw new BadCredentialsException('Invalid Credentials.');
            }
            $this->get('security.token_storage')->setToken(new UsernamePasswordToken($user,null,'main', $user->getRoles()));
            $this->get('session')->set('_security_main', serialize($this->get('security.token_storage')->getToken()));
            return $this->redirectToRoute('app_home');
            
        }
        return $this->render('security/login.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    #[Route('/register', name: 'app_register', methods:["GET", "POST"])]

    public function register(Request $request, UserPasswordEncoderInterface $passwordEncoder)
    {
        $form = $this->createForm(RegistrationType::class);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $formData = $form->getData();

            $user = new User();
            $user->setEmail($formData['email']);
            
            // Encode and set the password
            $encodedPassword = $passwordEncoder->encodePassword($user, $formData['password']);
            $user->setPassword($encodedPassword);

            // Persist the user to the database
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($user);
            $entityManager->flush();

            // Log in the user immediately after registration
            $token = new UsernamePasswordToken($user, null, 'main', $user->getRoles());
            $this->get('security.token_storage')->setToken($token);
            $this->get('session')->set('_security_main', serialize($token));

            // Redirect to a success page or any other desired route
            return $this->redirectToRoute('app_home'); // Replace with your desired route
        }

        return $this->render('security/login.html.twig', [
            'form' => $form->createView(),
        ]);
    }
        /**
     * @Route("/logout", name="app_logout")
     */
    public function logout(LogoutSuccessHandlerInterface $logoutSuccessHandler): Response
    {
        // The actual logout logic is handled by Symfony's security system
        // You can customize the logout behavior using the LogoutSuccessHandlerInterface

        // Redirect to the target URL defined in security.yaml after successful logout
        return $logoutSuccessHandler->onLogoutSuccess($this->getRequest());
    }

}
