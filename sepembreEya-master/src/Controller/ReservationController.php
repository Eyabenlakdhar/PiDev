<?php

namespace App\Controller;

use App\Entity\Reservation;
use App\Form\ReservationType;
use App\Repository\ReservationRepository;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Controller\MailerController;


#[Route('/reservation')]
class ReservationController extends AbstractController
{
    #[Route('/', name: 'app_reservation_index', methods: ['GET'])]
    public function index(ReservationRepository $reservationRepository): Response
    {
        return $this->render('reservation/index.html.twig', [
            'reservations' => $reservationRepository->findAll(),
        ]);
    }

    #[Route('/trieasc', name: 'app_trieascc', methods: ['GET'])]
public function ascendingAction(ReservationRepository $ReservationRepository)
{
    return $this->render('reservation/index.html.twig', [
        'reservations' => $ReservationRepository->findAllAscending(),
    ]);
}


#[Route('/triedesc', name: 'app_triedescc', methods: ['GET'])]
public function descendingAction(ReservationRepository $ReservationRepository)
{
   
    return $this->render('reservation/index.html.twig', [
        'reservations' => $ReservationRepository->findAllDescending(),
    ]);

}


    #[Route('/mesreservation', name: 'mesreservation', methods: ['GET'])]
public function mesreclamation(ReservationRepository $ReservationRepository)
{
    return $this->render('reservation/show.html copy.twig', [
        'reservations' => $ReservationRepository->findAll(),
    ]);
}

    #[Route('/new', name: 'app_reservation_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $reservation = new Reservation();
        $form = $this->createForm(ReservationType::class, $reservation);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($reservation);
            $entityManager->flush();

            return $this->redirectToRoute('app_reservation_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('reservation/new.html.twig', [
            'reservation' => $reservation,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_reservation_show', methods: ['GET'])]
    public function show(Reservation $reservation): Response
    {
        return $this->render('reservation/show.html.twig', [
            'reservation' => $reservation,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_reservation_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Reservation $reservation, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ReservationType::class, $reservation);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_reservation_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('reservation/edit.html.twig', [
            'reservation' => $reservation,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_reservation_delete', methods: ['POST'])]
    public function delete(Request $request, Reservation $reservation, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$reservation->getId(), $request->request->get('_token'))) {
            $entityManager->remove($reservation);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_reservation_index', [], Response::HTTP_SEE_OTHER);
    }

//     //#[Route('/sendmail/{id}', name: 'mailing',methods: ['GET'])]
//     public function sendEmail(Request $request, MailerController $mailer,UserRepository $userRepository,TokenGeneratorInterface  $tokenGenerator): Response
//     {

//         $form = $this->createForm(ForgotPasswordType::class);
//         $form->handleRequest($request);
//         if($form->isSubmitted()) {
//             $donnees = $form->getData();//


//             $user = $userRepository->findOneBy(['email'=>$donnees]);
//             if(!$user) {
//                 $this->addFlash('danger','cette adresse n\'existe pas');
//                 return $this->redirectToRoute("forgot");

//             }
//             $token = $tokenGenerator->generateToken();

//             try{
//                 $user->setResetToken($token);
//                 $entityManger = $this->getDoctrine()->getManager();
//                 $entityManger->persist($user);
//                 $entityManger->flush();




//             }catch(\Exception $exception) {
//                 $this->addFlash('warning','une erreur est survenue :'.$exception->getMessage());
//                 return $this->redirectToRoute("app_login");


//             }

//             $url = $this->generateUrl('app_reset_password',array('token'=>$token),UrlGeneratorInterface::ABSOLUTE_URL);
//         $email = (new Email())
//         ->from('kharrat.raed@esprit.tn')
//         //->To($User->getEmail())
//         ->To('kharrat.raed@esprit.tn')
//         ->subject('Confirmation de reservation')
//                 ->text("<p> Bonjour</p> Votre demande de reservation est bien recu . :".$url,
//                 "text/html");
//         // ->text('<p> Bonjour</p> unde demande de réinitialisation de mot de passe a été effectuée. Veuillez cliquer sur le lien suivant :".$url,
//         // "text/html');
       
//  try {
//         $mailer->send($email);
//         $this->addFlash('message','E-mail  de confirmation de votre reservation:');
//     } catch (TransportExceptionInterface $e) {
//         // Gérer les erreurs d'envoi de courriel
//     }

// }
// return $this->render("user/index.html.twig",['form'=>$form->createView()]);


//     }



// public function searchAction(Request $request, ReclamationRepository $reclamationRepository)
// {
//     $query = $request->get('query'); // Assuming the search query is passed as a query parameter

//     // Use the searchDql method to get filtered results
//     $reclamations = $reclamationRepository->searchDql($query);

//     // Handle and return $reclamations as needed
// }

}
